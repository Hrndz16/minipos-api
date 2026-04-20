package epw.minipos_api.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 1)
public class RateLimitFilter extends OncePerRequestFilter {

    private final Map<String, Deque<Long>> requestsByIp = new ConcurrentHashMap<>();
    private final int maxRequestsPerWindow;
    private final long windowMillis;

    public RateLimitFilter(
            @Value("${app.rate-limit.max-requests:10}") int maxRequestsPerWindow,
            @Value("${app.rate-limit.window-seconds:60}") long windowSeconds
    ) {
        this.maxRequestsPerWindow = maxRequestsPerWindow;
        this.windowMillis = windowSeconds * 1000;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String clientIp = extractClientIp(request);
        if (isRateLimitExceeded(clientIp)) {
            writeRateLimitResponse(response);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private boolean isRateLimitExceeded(String clientIp) {
        long now = System.currentTimeMillis();
        Deque<Long> timestamps = requestsByIp.computeIfAbsent(clientIp, ignored -> new ArrayDeque<>());

        synchronized (timestamps) {
            removeExpiredEntries(timestamps, now);
            if (timestamps.size() >= maxRequestsPerWindow) {
                return true;
            }
            timestamps.addLast(now);
            return false;
        }
    }

    private void removeExpiredEntries(Deque<Long> timestamps, long now) {
        long threshold = now - windowMillis;
        while (!timestamps.isEmpty() && timestamps.peekFirst() < threshold) {
            timestamps.removeFirst();
        }
    }

    private void writeRateLimitResponse(HttpServletResponse response) throws IOException {
        response.setStatus(429);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.getWriter().write("""
                {
                  "timestamp": "%s",
                  "status": 429,
                  "error": "Too Many Requests",
                  "message": "Request limit exceeded. Maximum 10 requests per minute per IP."
                }
                """.formatted(OffsetDateTime.now()));
    }

    private String extractClientIp(HttpServletRequest request) {
        String forwardedFor = request.getHeader("X-Forwarded-For");
        if (forwardedFor != null && !forwardedFor.isBlank()) {
            return forwardedFor.split(",")[0].trim();
        }

        String realIp = request.getHeader("X-Real-IP");
        if (realIp != null && !realIp.isBlank()) {
            return realIp.trim();
        }

        return request.getRemoteAddr();
    }
}
