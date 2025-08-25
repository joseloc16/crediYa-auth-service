package co.com.bancolombia.api.error;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "FieldError")
public record FieldError(
    @Schema(example = "firstName") String field,
    @Schema(example = "must not be blank") String message
) {}
