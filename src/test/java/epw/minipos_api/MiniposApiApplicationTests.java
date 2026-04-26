package epw.minipos_api;

import epw.minipos_api.Entity.RequestLog;
import epw.minipos_api.Repository.CustomerRepository;
import epw.minipos_api.Repository.RequestLogRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MiniposApiApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RequestLogRepository requestLogRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void contextLoads() {
    }

    @Test
    void shouldSaveIpWhenRequestHitsApi() throws Exception {
        long previousCount = requestLogRepository.count();

        mockMvc.perform(get("/customers")
                        .header("X-Forwarded-For", "203.0.113.10")
                        .header("User-Agent", "JUnit-Test"))
                .andExpect(status().isOk());

        long currentCount = requestLogRepository.count();
        assertThat(currentCount).isGreaterThan(previousCount);

        RequestLog latestLog = requestLogRepository.findAll(Sort.by(Sort.Direction.DESC, "id"))
                .stream()
                .findFirst()
                .orElseThrow();

        assertThat(latestLog.getIpAddress()).isEqualTo("203.0.113.10");
        assertThat(latestLog.getMethod()).isEqualTo("GET");
        assertThat(latestLog.getPath()).isEqualTo("/customers");
        assertThat(latestLog.getStatus()).isEqualTo(200);
    }

    @Test
    void shouldReturnConflictWhenEmailAlreadyExists() throws Exception {
        String email = "duplicate-test@example.com";
        customerRepository.findAll().stream()
                .filter(customer -> email.equalsIgnoreCase(customer.getEmail()))
                .forEach(customerRepository::delete);

        String payload = """
                {
                  "fullName": "Cliente Uno",
                  "email": "%s",
                  "phone": "3001234567"
                }
                """.formatted(email);

        mockMvc.perform(post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.message").value("A customer with email '" + email + "' already exists"));
    }

    @Test
    void shouldReturnAdminMenu() throws Exception {
        mockMvc.perform(get("/api/menu/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4))
                .andExpect(jsonPath("$[0].name").value("customers"))
                .andExpect(jsonPath("$[1].name").value("departments"))
                .andExpect(jsonPath("$[2].name").value("tmo"))
                .andExpect(jsonPath("$[3].name").value("about"));
    }

    @Test
    void shouldReturnBadRequestWhenRoleIsNotSupported() throws Exception {
        mockMvc.perform(get("/api/menu/99"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Role with id '99' is not supported"));
    }
}
