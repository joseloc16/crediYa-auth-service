package co.com.bancolombia.api;

import co.com.bancolombia.api.dto.SignUpRequest;
import co.com.bancolombia.api.mapper.UserEntryMapper;
import co.com.bancolombia.model.dto.Credentials;
import co.com.bancolombia.model.dto.SignUpCommand;
import co.com.bancolombia.usecase.auth.AuthUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class AuthHandler {

    private final AuthUseCase authUseCase;
    private final RequestValidator requestValidator;
    private final UserEntryMapper userEntryMapper;

    public Mono<ServerResponse> signUp(ServerRequest request) {
        return request.bodyToMono(SignUpRequest.class)
            .switchIfEmpty(Mono.error(new IllegalArgumentException("Body es requerido")))
            .flatMap(requestValidator::validateUser)
            .map(userEntryMapper::toCommand)
            .flatMap(authUseCase::signUp)
            .map(userEntryMapper::toResponse)
            .flatMap(u -> ServerResponse.status(201).contentType(MediaType.APPLICATION_JSON).bodyValue(u));
    }


    public Mono<ServerResponse> login(ServerRequest request) {
        return request.bodyToMono(Credentials.class)
            .flatMap(authUseCase::login)
            .flatMap(token -> ServerResponse.ok().bodyValue(token));
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Mono<ServerResponse> hello(ServerRequest request) {
        return ServerResponse.ok().bodyValue("Hello");
    }
}
