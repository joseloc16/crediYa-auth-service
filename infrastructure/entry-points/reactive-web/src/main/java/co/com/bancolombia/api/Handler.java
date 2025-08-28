package co.com.bancolombia.api;

import co.com.bancolombia.api.dto.CreateUserDTO;
import co.com.bancolombia.api.mapper.UserDTOMapper;
import co.com.bancolombia.model.commons.util.EmailUtils;
import co.com.bancolombia.usecase.user.input.UserUseCasePort;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class Handler {

    private final UserUseCasePort userUseCasePort;
    private final UserDTOMapper userMapper;
    private final RequestValidator requestValidator;

    @Transactional
    public Mono<ServerResponse> listenSaveUser(ServerRequest req) {
        final String path = req.path();
        return req.bodyToMono(CreateUserDTO.class)
            .switchIfEmpty(Mono.error(new ValidationException("El body es requerido")))
            .flatMap(requestValidator::validateUser)
            .doOnNext(dto -> log.debug("validated request email={}", EmailUtils.maskEmail(dto.email())))
            .map(userMapper::toModel)
            .flatMap(userUseCasePort::save)
            .map(userMapper::toResponse)
            .flatMap(dto -> ServerResponse.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto))
            .doOnSuccess(r -> log.info("POST {} <- 201 Created", path))
            .doOnError(e -> log.warn("POST {} <- error: {}", path, e.toString()));
    }

    public Mono<ServerResponse> listenFindByEmail(ServerRequest req) {
        final String email = req.queryParam("email")
            .orElseThrow(() -> new org.springframework.web.server.ServerWebInputException("email is required"));

        return userUseCasePort.findByEmail(email)
            .map(userMapper::toResponse)
            .flatMap(dto -> ServerResponse.ok()
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .bodyValue(dto))
            .switchIfEmpty(ServerResponse.notFound().build());
    }
}
