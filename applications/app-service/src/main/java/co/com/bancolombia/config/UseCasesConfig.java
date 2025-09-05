package co.com.bancolombia.config;

import co.com.bancolombia.model.security.PasswordEncoderGateway;
import co.com.bancolombia.model.token.TokenGateway;
import co.com.bancolombia.model.user.gateways.UserGateway;
import co.com.bancolombia.usecase.auth.AuthUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCasesConfig {
    @Bean
    public AuthUseCase logInUseCase(UserGateway userGateway, PasswordEncoderGateway passwordEncoderGateway, TokenGateway tokenGateway) {
        return new AuthUseCase(userGateway, passwordEncoderGateway, tokenGateway);
    }
}
