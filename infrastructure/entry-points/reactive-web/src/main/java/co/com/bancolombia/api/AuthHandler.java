package co.com.bancolombia.api;

import co.com.bancolombia.api.dto.SignUpRequest;
import co.com.bancolombia.api.mapper.UserEntryMapper;
import co.com.bancolombia.model.dto.Credentials;
import co.com.bancolombia.usecase.auth.AuthUseCase;
import co.com.bancolombia.usecase.getuserbydocument.input.GetUserByDocumentUseCasePort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class AuthHandler {

    private final AuthUseCase authUseCase;
    private final RequestValidator requestValidator;
    private final UserEntryMapper userEntryMapper;
    private final GetUserByDocumentUseCasePort getUserByDocumentUseCase;

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

    public Mono<ServerResponse> getByDocument(ServerRequest request) {
        final String documentNumber = request.pathVariable("documentNumber");
        if (documentNumber == null || documentNumber.isBlank()) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "documentNumber es requerido y no puede estar vacío"
            );
        }
        return getUserByDocumentUseCase.execute(documentNumber)
            .map(userEntryMapper::toResponse)
            .flatMap(body -> ServerResponse.ok().bodyValue(body))
            .switchIfEmpty(ServerResponse.status(HttpStatus.NOT_FOUND).build());
    }
}
