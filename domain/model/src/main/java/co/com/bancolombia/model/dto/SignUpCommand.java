package co.com.bancolombia.model.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Set;

public record SignUpCommand(
    String name,
    String lastName,
    String email,
    String password,
    String document,
    String phoneNumber,
    BigDecimal baseSalary,
    Timestamp birthDate,
    Set<String> roles
) {}
