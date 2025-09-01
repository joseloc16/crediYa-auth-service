package co.com.bancolombia.api.error;

import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebPropsConfig {
    @Bean
    WebProperties.Resources webResources() {
        return new WebProperties.Resources();
    }
}
