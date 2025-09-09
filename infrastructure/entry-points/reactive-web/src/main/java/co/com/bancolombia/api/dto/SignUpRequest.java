package co.com.bancolombia.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Set;

public record SignUpRequest(

    @NotNull(message = "Nombres es requerido")
    @NotBlank(message = "Nombres no debe estar vacio")
    @JsonProperty("nombres")
    String name,

    @NotNull(message = "Apellidos es requerido")
    @NotBlank(message = "Apellidos no debe estar vacio")
    @JsonProperty("apellidos")
    String lastName,

    @NotNull(message = "Correo electronico es requerido")
    @NotBlank(message = "Correo electronico no debe estar vacio")
    @Email(message = "Formato de email no es valido",
        regexp = "^(?!\\.)[A-Za-z0-9._%+-]+(?<!\\.)@"
            + "[A-Za-z0-9](?:[A-Za-z0-9-]{0,61}[A-Za-z0-9])?"
            + "(?:\\.[A-Za-z0-9](?:[A-Za-z0-9-]{0,61}[A-Za-z0-9])?)*"
            + "\\.[A-Za-z]{2,}$")
    @JsonProperty("correo_electronico")
    String email,

    @NotBlank(message = "Contrasena es requerido")
    @JsonProperty("contrasena")
    String password,

    @JsonProperty("documento_identidad")
    String documentNumber,// validar si debe ser obligatorio y unico

    @Pattern(
        regexp = "^\\+?[0-9. ()-]{7,25}$",
        message = "Formato de telefono no es valido"
    )
    @JsonProperty("telefono")
    String phoneNumber,// +51 912345678, (01) 234-5678, 1234567.

    @NotNull(message = "Salario base es requerido")
    @DecimalMin(value = "0", message = "Salario base debe ser >= 0")
    @DecimalMax(value = "15000000", message = "Salario base debe ser <= 15000000")
    @JsonProperty("salario_base")
    BigDecimal baseSalary,

    @JsonProperty("fecha_nacimiento")
    Timestamp birthDate,

    Set<String> roles // TODO: si viene vacio asignar rol por defecto "USER"
) {
}
