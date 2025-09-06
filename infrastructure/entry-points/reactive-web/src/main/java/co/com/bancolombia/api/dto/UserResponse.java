package co.com.bancolombia.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Set;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserResponse(
    String name,
    String lastName,
    String email,
    String document,
    String phoneNumber,
    BigDecimal baseSalary,
    Timestamp birthDate,
    Set<String> roles
) {}