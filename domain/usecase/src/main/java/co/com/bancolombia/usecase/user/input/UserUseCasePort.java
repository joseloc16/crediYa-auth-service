package co.com.bancolombia.usecase.user.input;

import co.com.bancolombia.model.user.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserUseCasePort {
    Mono<User> saveUser(User user);
    Mono<User> updateUser(User user);
    Flux<User> getAllUsers();
    Mono<User> getUserById(String id);
    Mono<Boolean> existsByEmail(String email);
    Mono<Boolean> existsByDocument(String document);
}
