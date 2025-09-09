package co.com.bancolombia.usecase.getuserbydocument.input;

import co.com.bancolombia.model.user.User;
import reactor.core.publisher.Mono;

public interface GetUserByDocumentUseCasePort {
    Mono<User> execute(String documentNumber);
}
