package co.com.bancolombia.usecase.user.input;

import co.com.bancolombia.model.user.User;
import reactor.core.publisher.Mono;

public interface UserUseCasePort {
    Mono<User> save(User user);
    Mono<User> findByEmail(String email);
}
