package co.com.bancolombia.model.user;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Set;
import java.util.UUID;

public record User(
    UUID id,
    String name,
    String lastName,
    String email,
    String documentNumber,
    String phoneNumber,
    BigDecimal baseSalary,
    Timestamp birthDate,
    Set<String> roles,
    String passwordHash
) {}
