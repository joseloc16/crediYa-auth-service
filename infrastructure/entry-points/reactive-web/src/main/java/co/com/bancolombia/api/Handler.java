package co.com.bancolombia.api;

import co.com.bancolombia.api.dto.CreateUserDTO;
import co.com.bancolombia.api.dto.EditUserDTO;
import co.com.bancolombia.api.mapper.UserDTOMapper;
import co.com.bancolombia.usecase.user.input.UserUseCasePort;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class Handler {

    private final UserUseCasePort userUseCasePort;
    private final UserDTOMapper userMapper;
    private final RequestValidator requestValidator;

    @Transactional
    public Mono<ServerResponse> listenSaveUser(ServerRequest req) {
        return req.bodyToMono(CreateUserDTO.class)
            .switchIfEmpty(Mono.error(new ValidationException("El body es requerido")))
            .flatMap(requestValidator::validateUser)
            .map(userMapper::toModel)
            .flatMap(userUseCasePort::saveUser)
            .map(userMapper::toResponse)
            .flatMap(dto -> ServerResponse.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto))
            .onErrorResume(ValidationException.class, e ->
                ServerResponse.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(Map.of(
                        "message", "Validation failed",
                        "details", e.getMessage()
                    ))
            );
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
}
