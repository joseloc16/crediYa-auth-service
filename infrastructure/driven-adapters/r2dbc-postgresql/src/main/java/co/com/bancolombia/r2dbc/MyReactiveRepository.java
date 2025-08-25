package co.com.bancolombia.r2dbc;

import co.com.bancolombia.r2dbc.entity.UserEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface MyReactiveRepository extends ReactiveCrudRepository<UserEntity, String>, ReactiveQueryByExampleExecutor<UserEntity> {
    Mono<Void> deleteById(String id);

    Mono<Boolean> existsByEmail(String email);

    Mono<Boolean> existsByDocument(String document);
}
