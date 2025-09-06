package co.com.bancolombia.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LogInRequest(

    @NotBlank(message = "Email es requerido")
    @Email(message = "Formato de email no es valido",
        regexp = "^(?!\\.)[A-Za-z0-9._%+-]+(?<!\\.)@"
            + "[A-Za-z0-9](?:[A-Za-z0-9-]{0,61}[A-Za-z0-9])?"
            + "(?:\\.[A-Za-z0-9](?:[A-Za-z0-9-]{0,61}[A-Za-z0-9])?)*"
            + "\\.[A-Za-z]{2,}$")
    String email,

    @NotBlank(message = "Password es requerido")
    String password
) {
}
