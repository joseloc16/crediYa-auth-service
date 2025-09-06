package co.com.bancolombia.api.error;

import co.com.bancolombia.model.commons.exceptions.EmailAlreadyUsedException;
import co.com.bancolombia.model.commons.exceptions.RoleNotFoundException;
import jakarta.validation.ValidationException;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ResponseStatusException;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

@Component
public class GlobalErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        Throwable ex = getError(request);
        HttpStatus status = resolveStatus(ex);
        String code = resolveCode(ex);

        DateTimeFormatter FRIENDLY_TS = DateTimeFormatter
            .ofPattern("yyyy-MM-dd HH:mm:ss XXX")
            .withLocale(new Locale("es", "ES"));

        ZoneId zone = ZoneId.systemDefault();

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", ZonedDateTime.now(zone).format(FRIENDLY_TS));
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("code", code);
        body.put("message", resolveMessage(ex));

        if (ex instanceof jakarta.validation.ConstraintViolationException cve) {
            body.put("details", cve.getConstraintViolations().stream()
                .map(v -> Map.of("field", v.getPropertyPath().toString(),
                    "message", v.getMessage()))
                .toList());
        } else if (ex instanceof org.springframework.web.bind.support.WebExchangeBindException bind) {
            body.put("details", bind.getFieldErrors().stream()
                .map(fe -> Map.of("field", fe.getField(),
                    "message", fe.getDefaultMessage()))
                .toList());
        } else if (ex instanceof RoleNotFoundException rnfe) {
            body.put("details", java.util.List.of(
                Map.of("field", "roleId", "message", rnfe.getMessage())
            ));
        }

        return body;
    }

    private HttpStatus resolveStatus(Throwable ex) {
        if (ex instanceof EmailAlreadyUsedException) return HttpStatus.CONFLICT;
        if (ex instanceof org.springframework.dao.DuplicateKeyException) return HttpStatus.CONFLICT;
        if (ex instanceof RoleNotFoundException) return HttpStatus.NOT_FOUND;
        if (ex instanceof jakarta.validation.ConstraintViolationException) return HttpStatus.BAD_REQUEST;
        if (ex instanceof ValidationException) return HttpStatus.BAD_REQUEST;
        if (ex instanceof org.springframework.web.server.ResponseStatusException rse)
            return HttpStatus.valueOf(rse.getStatusCode().value());
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

    private String resolveCode(Throwable ex) {
        if (ex instanceof EmailAlreadyUsedException) return "El email ya está en uso.";
        if (ex instanceof RoleNotFoundException) return "El rol especificado no existe.";
        if (ex instanceof DuplicateKeyException dke) return humanizeDuplicate(dke);
        if (ex instanceof jakarta.validation.ConstraintViolationException
            || ex instanceof ValidationException
            || ex instanceof WebExchangeBindException) return "VALIDATION_ERROR";
        if (ex instanceof ResponseStatusException) return "ERROR";
        return "INTERNAL_ERROR";
    }

    private String resolveMessage(Throwable ex) {
        if (ex instanceof ResponseStatusException rse) {
            return rse.getReason() != null ? rse.getReason() : rse.getMessage();
        }
        return ex.getMessage();
    }

    private String humanizeDuplicate(DuplicateKeyException ex) {
        String msg = ex.getMostSpecificCause() != null ? ex.getMostSpecificCause().getMessage() : ex.getMessage();
        if (msg != null) {
            String m = msg.toLowerCase();
            if (m.contains("uq_usuario_email")) return "El email ya esta en uso.";
            if (m.contains("uq_usuario_documento")) return "El documento ya esta en uso.";
        }
        return "Registro duplicado.";
    }
}
