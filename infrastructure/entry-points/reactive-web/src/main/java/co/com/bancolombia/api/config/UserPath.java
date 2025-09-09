package co.com.bancolombia.api.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "routes")
public class UserPath {

    private String base;
    private Paths paths;

    @Getter
    @Setter
    public static class Paths {
        private String login;
        private String signup;
        private String byDocument;
    }
}
