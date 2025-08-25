package co.com.bancolombia.api;

import co.com.bancolombia.api.dto.CreateUserDTO;
import co.com.bancolombia.api.dto.EditUserDTO;
import co.com.bancolombia.api.mapper.UserDTOMapper;
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

import java.util.Map;

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
            .doOnNext(dto -> log.debug("validated request email={}", maskEmail(dto.email())))
            .map(userMapper::toModel)
            .flatMap(userUseCasePort::saveUser)
            .map(userMapper::toResponse)
            .flatMap(dto -> ServerResponse.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto))
            .doOnSuccess(r -> log.info("POST {} <- 201 Created", path))
            .doOnError(e -> log.warn("POST {} <- error: {}", path, e.toString()));
    }

    public Mono<ServerResponse> listenUpdateUser(ServerRequest req) {
        return req.bodyToMono(EditUserDTO.class)
            .map(userMapper::toModel)
            .flatMap(userUseCasePort::updateUser)
            .map(userMapper::toResponse)
            .flatMap(dto -> ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto));
    }

    public Mono<ServerResponse> listenGetAllUsers(ServerRequest req) {
        return userUseCasePort.getAllUsers()
            .collectList()
            .map(userMapper::toResponseList)
            .flatMap(list -> ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(list));
    }

    public Mono<ServerResponse> listenGetUserById(ServerRequest req) {
        String id = req.pathVariable("id");
        return userUseCasePort.getUserById(id)
            .map(userMapper::toResponse)
            .flatMap(dto -> ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto))
            .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> listenExistsByEmail(ServerRequest req) {
        return Mono.justOrEmpty(req.queryParam("email"))
            .flatMap(userUseCasePort::existsByEmail)
            .flatMap(exists -> ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Map.of("exists", exists)))
            .switchIfEmpty(ServerResponse.badRequest()
                .bodyValue(Map.of("error", "email query param is required")));
    }

    public Mono<ServerResponse> listenExistsByDocument(ServerRequest req) {
        return Mono.justOrEmpty(req.queryParam("document"))
            .flatMap(userUseCasePort::existsByDocument)
            .flatMap(exists -> ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Map.of("exists", exists)))
            .switchIfEmpty(ServerResponse.badRequest()
                .bodyValue(Map.of("error", "document query param is required")));
    }

    private static String maskEmail(String email) {
        if (email == null) return null;
        int at = email.indexOf('@');
        if (at <= 1) return "***" + email.substring(Math.max(at, 0));
        return email.charAt(0) + "***" + email.substring(at);
    }
}
