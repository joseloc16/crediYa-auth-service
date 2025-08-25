package co.com.bancolombia.usecase.user;

import co.com.bancolombia.model.user.User;
import co.com.bancolombia.model.user.gateways.UserRepository;
import co.com.bancolombia.usecase.user.exception.EmailAlreadyUsedException;
import co.com.bancolombia.usecase.user.input.UserUseCasePort;
import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

//@Slf4j
@RequiredArgsConstructor
public class UserUseCase implements UserUseCasePort {

    private final UserRepository userRepository;

    @Override
    public Mono<User> saveUser(User user) {
        String emailNormalized = user.getEmail().strip().toLowerCase(java.util.Locale.ROOT);
        //log.debug("saveUser email={}", emailNormalized);
        return userRepository.existsByEmail(user.getEmail().trim().toLowerCase())
            //.doOnNext(exists -> log.debug("existsByEmail email={} -> {}", emailNormalized, exists))
            .flatMap(exists -> {
                if (Boolean.TRUE.equals(exists)) {
                    //log.info("rejecting create: email already used email={}", emailNormalized);
                    return Mono.error(new EmailAlreadyUsedException(user.getEmail()));
                }
                return userRepository.save(user.withEmail(emailNormalized));
                    //.doOnSuccess(u -> log.info("user created id={} email={}", u.getId(), normalized));
            });
    }

    @Override
    public Mono<User> updateUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public Flux<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Mono<User> getUserById(String id) {
        return userRepository.findById(id);
    }

    @Override
    public Mono<Boolean> existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public Mono<Boolean> existsByDocument(String document) {
        return userRepository.existsByDocument(document);
    }
}
