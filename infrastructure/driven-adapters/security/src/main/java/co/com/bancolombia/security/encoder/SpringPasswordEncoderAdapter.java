package co.com.bancolombia.security.encoder;

import co.com.bancolombia.model.security.PasswordEncoderGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SpringPasswordEncoderAdapter implements PasswordEncoderGateway {

    private final PasswordEncoder delegate;

    @Override
    public String encode(String raw) {
        return delegate.encode(raw);
    }

    @Override
    public boolean matches(String raw, String hash) {
        return delegate.matches(raw, hash);
    }
}
