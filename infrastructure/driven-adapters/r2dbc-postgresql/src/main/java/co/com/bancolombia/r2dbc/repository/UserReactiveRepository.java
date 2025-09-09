package co.com.bancolombia.r2dbc.repository;

import co.com.bancolombia.r2dbc.entity.UserEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface UserReactiveRepository extends ReactiveCrudRepository<UserEntity, String>, ReactiveQueryByExampleExecutor<UserEntity> {
    @Query("select exists(select 1 from usuario where email = :email)")
    Mono<Boolean> existsByEmail(String email);

    @Query("select * from usuario where email = :email")
    Mono<UserEntity> findByEmail(String email);

    @Query("select * from usuario where documento_identidad = :documentNumber")
    Mono<UserEntity> findByDocumentNumber(String documentNumber);
}
