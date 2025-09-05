package co.com.bancolombia.config;

import co.com.bancolombia.model.user.gateways.UserGateway;
import co.com.bancolombia.usecase.auth.AuthUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCasesConfig {
    @Bean
    public AuthUseCase logInUseCase(UserGateway userGateway) {
        return new AuthUseCase(userGateway);
    }
}
