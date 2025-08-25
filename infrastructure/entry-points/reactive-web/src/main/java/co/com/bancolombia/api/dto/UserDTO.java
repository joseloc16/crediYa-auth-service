package co.com.bancolombia.api.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;

public record UserDTO(
    String firstName,
    String lastName,
    String email,
    String identityDocument,
    String phoneNumber,
    String roleId,
    BigDecimal baseSalary,
    Timestamp birthDate) {
}
