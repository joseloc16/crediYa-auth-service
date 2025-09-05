package co.com.bancolombia.api;

import co.com.bancolombia.model.dto.LogInDTO;
import co.com.bancolombia.model.dto.SignUpDTO;
import co.com.bancolombia.usecase.auth.AuthUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class AuthHandler {

    private final AuthUseCase authUseCase;

    public Mono<ServerResponse> signUp(ServerRequest request) {
        return request.bodyToMono(SignUpDTO.class)
            .flatMap(authUseCase::signUp)
            .flatMap(u -> ServerResponse.status(201).bodyValue(u));
    }

    public Mono<ServerResponse> login(ServerRequest request) {
        return request.bodyToMono(LogInDTO.class)
            .flatMap(authUseCase::login)
            .flatMap(token -> ServerResponse.ok().bodyValue(token));
    }

    public Mono<ServerResponse> hello(ServerRequest request) {
        return ServerResponse.ok().bodyValue("Hello");
    }
}
