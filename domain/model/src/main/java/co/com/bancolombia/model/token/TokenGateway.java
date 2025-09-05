package co.com.bancolombia.model.token;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface TokenGateway {
    String generateToken(String username, Set<String> roles);

    String getSubject(String token);

    boolean validate(String token);

    Map<String, Object> getAllClaims(String token);

    Optional<Object> getClaim(String token, String name);

    Set<String> getRoles(String token);
}
