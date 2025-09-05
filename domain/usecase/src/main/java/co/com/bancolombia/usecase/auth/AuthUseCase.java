package co.com.bancolombia.usecase.auth;

import co.com.bancolombia.model.dto.LogInDTO;
import co.com.bancolombia.model.dto.SignUpDTO;
import co.com.bancolombia.model.dto.TokenDTO;
import co.com.bancolombia.model.user.User;
import co.com.bancolombia.model.user.gateways.UserGateway;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
public class AuthUseCase {

    private final UserGateway userGateway;

    public Mono<User> signUp(SignUpDTO dto) {
        return userGateway.existsByEmail(dto.email())
            .flatMap(exists -> {
                if (exists) {
                    return Mono.error(new IllegalArgumentException("email already in use"));
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
                    dto.password()
                );

                return userGateway.save(u);
            });
    }

    public Mono<TokenDTO> login(LogInDTO dto) {
        return null;
    }
}
