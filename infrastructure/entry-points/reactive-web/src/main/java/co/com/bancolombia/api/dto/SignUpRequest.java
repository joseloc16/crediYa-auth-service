package co.com.bancolombia.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Set;

public record SignUpRequest(

    @NotBlank(message = "Nombre es requerido")
    String name,

    @NotBlank(message = "Apellido es requerido")
    String lastName,

    @NotBlank(message = "Email es requerido")
    @Email(message = "Formato de email no es valido",
        regexp = "^(?!\\.)[A-Za-z0-9._%+-]+(?<!\\.)@"
            + "[A-Za-z0-9](?:[A-Za-z0-9-]{0,61}[A-Za-z0-9])?"
            + "(?:\\.[A-Za-z0-9](?:[A-Za-z0-9-]{0,61}[A-Za-z0-9])?)*"
            + "\\.[A-Za-z]{2,}$")
    String email,

    @NotBlank(message = "Password es requerido")
    String password,

    String document,// validar si debe ser obligatorio y unico

    @Pattern(
        regexp = "^\\+?[0-9. ()-]{7,25}$",
        message = "phoneNumber format is not valid"
    )
    String phoneNumber,// +51 912345678, (01) 234-5678, 1234567.

    @DecimalMin(value = "0", message = "baseSalary must be >= 0")
    @DecimalMax(value = "15000000", message = "baseSalary must be <= 15000000")
    BigDecimal baseSalary,

    Timestamp birthDate,

    Set<String> roles // TODO: si viene vacio asignar rol por defecto "USER"
) {
}
