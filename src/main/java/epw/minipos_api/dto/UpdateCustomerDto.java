package epw.minipos_api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UpdateCustomerDto(
        @Size(max = 120, message = "fullName must be at most 120 characters")
        String fullName,

        @Email(message = "email must be valid")
        @Size(max = 255, message = "email must be at most 255 characters")
        String email,

        @Size(max = 30, message = "phone must be at most 30 characters")
        String phone
) {
}
