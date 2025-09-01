package co.com.bancolombia.api;

import co.com.bancolombia.api.config.UserPath;
import co.com.bancolombia.api.dto.CreateUserDTO;
import co.com.bancolombia.api.dto.UserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
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
    @RouterOperations({
        @RouterOperation(
            path = "/api/v1/usuarios",
            method = RequestMethod.POST,
            beanClass = Handler.class,
            beanMethod = "listenSaveUser",
            operation = @Operation(
                operationId = "createUser",
                summary = "Create user",
                requestBody = @RequestBody(required = true,
                    content = @Content(schema = @Schema(implementation = CreateUserDTO.class))),
                responses = {
                    @ApiResponse(
                        responseCode = "201",
                        description = "Created",
                        content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserDTO.class)
                        )
                    ),
                    @ApiResponse(
                        responseCode = "400",
                        description = "Validation error",
                        content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = co.com.bancolombia.api.error.ApiError.class),
                            examples = @ExampleObject(
                                name = "validation-400",
                                value = """
                                    {
                                      "timestamp": "2025-08-25 03:34:20 -05:00",
                                      "status": 400,
                                      "error": "Bad Request",
                                      "code": "VALIDATION_ERROR",
                                      "message": "[{field=firstName, message=firstName is required}, {field=lastName, message=lastName is required}]"
                                    }
                                    """
                            )
                        )
                    ),
                    @ApiResponse(
                        responseCode = "409",
                        description = "Email already registered",
                        content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = co.com.bancolombia.api.error.ApiError.class),
                            examples = @ExampleObject(
                                name = "conflict-409",
                                value = """
                                    {
                                      "timestamp": "2025-08-25 03:49:30 -05:00",
                                      "status": 409,
                                      "error": "Conflict",
                                      "code": "EMAIL_EXISTS",
                                      "message": "email already registered: diana.benites@test.com"
                                    }
                                    """
                            )
                        )
                    )
                }
            )
        )
    })
    public RouterFunction<ServerResponse> routerFunction() {
        return route(POST(userPath.getUser()), userHandler::listenSaveUser)
            .andRoute(
                GET(userPath.getUser()).and(queryParam("email", v -> true)),
                userHandler::listenFindByEmail
            );
    }
}
