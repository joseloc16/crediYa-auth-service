package co.com.bancolombia.api;

import co.com.bancolombia.api.config.UserPath;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;

@Configuration
@RequiredArgsConstructor
public class RouterAuth {

    private final UserPath userPath;

    @Bean
    public RouterFunction<ServerResponse> routerFunction(AuthHandler authHandler) {
        return RouterFunctions
            .route(POST(userPath.getBase() + userPath.getPaths().getSignup()), authHandler::signUp)
            .andRoute(POST(userPath.getBase() + userPath.getPaths().getLogin()), authHandler::login)
            .andRoute(GET(userPath.getBase() + userPath.getPaths().getByDocument()), authHandler::getByDocument)
            .andRoute(GET("/hello"), authHandler::hello);
    }
}
