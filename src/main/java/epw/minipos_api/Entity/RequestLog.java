package epw.minipos_api.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.time.OffsetDateTime;

@Entity
@Table(name = "request_logs")
public class RequestLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ip_address", nullable = false, length = 45)
    private String ipAddress;

    @Column(nullable = false, length = 10)
    private String method;

    @Column(nullable = false, length = 255)
    private String path;

    @Column(nullable = false)
    private Integer status;

    @Column(name = "user_agent", length = 500)
    private String userAgent;

    @Column(name = "requested_at", nullable = false, updatable = false)
    private OffsetDateTime requestedAt;

    protected RequestLog() {
    }

    public RequestLog(String ipAddress, String method, String path, Integer status, String userAgent) {
        this.ipAddress = ipAddress;
        this.method = method;
        this.path = path;
        this.status = status;
        this.userAgent = userAgent;
    }

    @PrePersist
    void prePersist() {
        if (requestedAt == null) {
            requestedAt = OffsetDateTime.now();
        }
    }

    public Long getId() {
        return id;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public Integer getStatus() {
        return status;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public OffsetDateTime getRequestedAt() {
        return requestedAt;
    }
}
