package co.com.bancolombia.r2dbc.adapter;

import co.com.bancolombia.model.user.User;
import co.com.bancolombia.model.user.gateways.UserGateway;
import co.com.bancolombia.r2dbc.repository.UserReactiveRepository;
import co.com.bancolombia.r2dbc.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserR2dbcAdapter implements UserGateway {

    private final UserReactiveRepository userRepository;
    private final TransactionalOperator tx;
    private final UserMapper mapper;

    @Override
    public Mono<Boolean> existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public Mono<User> save(User user) {
        return userRepository.save(mapper.toEntity(user))
            .map(mapper::toDomain)
            .as(tx::transactional);
    }

    @Override
    public Mono<User> findByEmail(String email) {
        return userRepository.findByEmail(email)
            .doOnNext(e -> log.info("User found: {}", e.getName()))
            .map(mapper::toDomain);
    }

    @Override
    public Mono<User> findByDocumentNumber(String documentNumber) {
        return userRepository.findByDocumentNumber(documentNumber)
            .doOnNext(e -> log.info("User found: {}", e.getName()))
            .map(mapper::toDomain);
    }
}

