package co.com.bancolombia.config;

import co.com.bancolombia.model.user.gateways.UserRepository;
import co.com.bancolombia.usecase.user.UserUseCase;
import co.com.bancolombia.usecase.user.input.UserUseCasePort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCasesConfig {

        @Bean
        public UserUseCasePort userUseCasePort(UserRepository repository) {
                return new UserUseCase(repository);
        }
}
