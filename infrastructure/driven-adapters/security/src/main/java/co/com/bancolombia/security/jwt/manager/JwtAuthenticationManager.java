package co.com.bancolombia.security.jwt.manager;

import co.com.bancolombia.model.token.TokenGateway;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@Component
public class JwtAuthenticationManager implements ReactiveAuthenticationManager {

    private final TokenGateway tokenGateway;

    public JwtAuthenticationManager(TokenGateway tokenGateway) {
        this.tokenGateway = tokenGateway;
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        final String token = String.valueOf(authentication.getCredentials());

        return Mono.fromSupplier(() -> {
            if (!tokenGateway.validate(token)) {
                throw new IllegalArgumentException("bad token");
            }
            var subject = tokenGateway.getSubject(token);
            var authorities = tokenGateway.getRoles(token).stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

            return new UsernamePasswordAuthenticationToken(subject, null, authorities);
        });
    }
}
