package co.com.bancolombia.usecase.auth;

import co.com.bancolombia.model.dto.Credentials;
import co.com.bancolombia.model.dto.SignUpCommand;
import co.com.bancolombia.model.dto.TokenDTO;
import co.com.bancolombia.model.security.PasswordEncoderGateway;
import co.com.bancolombia.model.token.TokenGateway;
import co.com.bancolombia.model.user.User;
import co.com.bancolombia.model.user.gateways.UserGateway;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
public class AuthUseCase {

    private final UserGateway userGateway;
    private final PasswordEncoderGateway passwordEncoder;
    private final TokenGateway tokenGateway;

    public Mono<User> signUp(SignUpCommand dto) {
        return userGateway.existsByEmail(dto.email())
            .flatMap(exists -> {
                if (exists) {
                    return Mono.error(new IllegalArgumentException("Email ya registrado"));
                }
                Set<String> roles =
                    (dto.roles() == null || dto.roles().isEmpty())
                        ? Set.of("ROLE_USER")
                        : new HashSet<>(dto.roles());

                User u = new User(
                    null,
                    dto.name(),
                    dto.lastName(),
                    dto.email(),
                    dto.document(),
                    dto.phoneNumber(),
                    dto.baseSalary(),
                    dto.birthDate(),
                    roles,
                    passwordEncoder.encode(dto.password())
                );

                return userGateway.save(u);
            });
    }

    public Mono<TokenDTO> login(Credentials dto) {
        return userGateway.findByEmail(dto.email())
            .switchIfEmpty(Mono.error(new IllegalArgumentException("bad credentials")))
            .flatMap(u -> passwordEncoder.matches(dto.password(), u.passwordHash())
                ? Mono.just(new TokenDTO(tokenGateway.generateToken(u.email(), u.roles())))
                : Mono.error(new IllegalArgumentException("bad credentials")));
    }
}
