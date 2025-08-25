package co.com.bancolombia.api.error;

import co.com.bancolombia.usecase.user.exception.EmailAlreadyUsedException;
import co.com.bancolombia.usecase.user.exception.RoleNotFoundException;
import jakarta.validation.ValidationException;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

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
        if (ex instanceof EmailAlreadyUsedException
            || ex instanceof org.springframework.dao.DuplicateKeyException) return "EMAIL_EXISTS";
        if (ex instanceof RoleNotFoundException) return "ROLE_NOT_FOUND";
        if (ex instanceof jakarta.validation.ConstraintViolationException
            || ex instanceof ValidationException
            || ex instanceof org.springframework.web.bind.support.WebExchangeBindException) return "VALIDATION_ERROR";
        if (ex instanceof org.springframework.web.server.ResponseStatusException) return "ERROR";
        return "INTERNAL_ERROR";
    }

    private String resolveMessage(Throwable ex) {
        if (ex instanceof org.springframework.web.server.ResponseStatusException rse) {
            return rse.getReason() != null ? rse.getReason() : rse.getMessage();
        }
        return ex.getMessage();
    }
}
