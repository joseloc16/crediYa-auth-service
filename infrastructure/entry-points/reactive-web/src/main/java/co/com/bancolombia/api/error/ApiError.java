package co.com.bancolombia.api.error;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ApiError")
public record ApiError(
    @Schema(example = "2025-08-25 03:49:30 -05:00") String timestamp,
    @Schema(example = "409") int status,
    @Schema(example = "Conflict") String error,
    @Schema(example = "EMAIL_EXISTS") String code,
    @Schema(example = "email already registered: jose.morocho@test.com") String message
) {}
