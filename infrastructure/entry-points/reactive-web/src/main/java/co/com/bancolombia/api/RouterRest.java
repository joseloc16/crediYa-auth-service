package co.com.bancolombia.api;

import co.com.bancolombia.api.config.UserPath;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@RequiredArgsConstructor
public class RouterRest {

    private final UserPath userPath;
    private final Handler userHandler;

    @Bean
    public RouterFunction<ServerResponse> routerFunction() {
        return route(POST(userPath.getUsers()), userHandler::listenSaveUser)
            .andRoute(PUT(userPath.getUsers()), userHandler::listenUpdateUser)
            .andRoute(GET(userPath.getUsers()), userHandler::listenGetAllUsers)
            .andRoute(GET(userPath.getUsersById()), userHandler::listenGetUserById)
            .andRoute(DELETE(userPath.getUsersById()), userHandler::listenDeleteUser)
            .andRoute(GET(userPath.getUsersExists()), userHandler::listenExistsByEmail)
            .andRoute(GET(userPath.getUsersExists()).and(queryParam("email", s -> true)), userHandler::listenExistsByEmail)
            .andRoute(GET(userPath.getUsersExists()).and(queryParam("document", s -> true)), userHandler::listenExistsByDocument);
    }
}
