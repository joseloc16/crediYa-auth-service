package co.com.bancolombia.api.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.sql.Timestamp;

public record EditUserDTO(

    @NotBlank(message = "firstName is required")
    String firstName,

    @NotBlank(message = "lastName is required")
    String lastName,

    @NotBlank(message = "email is required")
    @Email(message = "email format is not valid",
        regexp = "^(?!\\.)[A-Za-z0-9._%+-]+(?<!\\.)@"
            + "[A-Za-z0-9](?:[A-Za-z0-9-]{0,61}[A-Za-z0-9])?"
            + "(?:\\.[A-Za-z0-9](?:[A-Za-z0-9-]{0,61}[A-Za-z0-9])?)*"
            + "\\.[A-Za-z]{2,}$")
    String email,

    String document,

    String phoneNumber,

    String roleId,

    @DecimalMin(value = "0", message = "baseSalary must be >= 0")
    @DecimalMax(value = "15000000", message = "baseSalary must be <= 15000000")
    BigDecimal baseSalary,

    Timestamp birthDate) {
}
