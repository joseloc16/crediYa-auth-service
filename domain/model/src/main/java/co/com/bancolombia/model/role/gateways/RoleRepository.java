package co.com.bancolombia.model.role.gateways;

import reactor.core.publisher.Mono;

public interface RoleRepository {
    Mono<Boolean> existsById(String id);
}
