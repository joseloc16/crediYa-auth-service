package co.com.bancolombia.model.user.gateways;

import co.com.bancolombia.model.user.User;
import reactor.core.publisher.Mono;

public interface UserGateway {
    Mono<Boolean> existsByEmail(String email);

    Mono<User> save(User user);

    Mono<User> findByEmail(String email);

    Mono<User> findByDocumentNumber(String documentNumber);
}
