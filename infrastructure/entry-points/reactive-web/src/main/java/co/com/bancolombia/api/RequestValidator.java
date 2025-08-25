package co.com.bancolombia.api;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Validator;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class RequestValidator {

    private final Validator validator;

    public <T> Mono<T> validateUser(T dto) {
        return Mono.fromCallable(() -> {
            var errors = new BeanPropertyBindingResult(dto, dto.getClass().getName());
            validator.validate(dto, errors);
            if (errors.hasErrors()) {
                var details = errors.getFieldErrors().stream()
                    .map(fe -> Map.of("field", fe.getField(), "message", fe.getDefaultMessage()))
                    .toList();
                throw new ValidationException(details.toString());
            }
            return dto;
        });
    }

}
