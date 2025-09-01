package co.com.bancolombia.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.sql.Timestamp;

public record CreateUserDTO(

    @Schema(example = "Jose")
    @NotBlank(message = "firstName is required")
    String firstName,

    @Schema(example = "Morocho")
    @NotBlank(message = "lastName is required")
    String lastName,

    @Schema(example = "jose.morocho@test.com")
    @NotBlank(message = "email is required")
    @Email(message = "email format is not valid",
        regexp = "^(?!\\.)[A-Za-z0-9._%+-]+(?<!\\.)@"
            + "[A-Za-z0-9](?:[A-Za-z0-9-]{0,61}[A-Za-z0-9])?"
            + "(?:\\.[A-Za-z0-9](?:[A-Za-z0-9-]{0,61}[A-Za-z0-9])?)*"
            + "\\.[A-Za-z]{2,}$")
    String email,

    @Schema(example = "87654321")
    String document,

    @Schema(example = "+51 912345678")
    String phoneNumber,

    @Schema(example = "1")
    String roleId,

    @Schema(example = "4500.00")
    @DecimalMin(value = "0", message = "baseSalary must be >= 0")
    @DecimalMax(value = "15000000", message = "baseSalary must be <= 15000000")
    BigDecimal baseSalary,

    @Schema(example = "1995-08-24T00:00:00Z")
    Timestamp birthDate) {
}
