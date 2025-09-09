package co.com.bancolombia.usecase.getuserbydocument;

import co.com.bancolombia.model.commons.exceptions.UserNotFoundException;
import co.com.bancolombia.model.user.User;
import co.com.bancolombia.model.user.gateways.UserGateway;
import co.com.bancolombia.usecase.getuserbydocument.input.GetUserByDocumentUseCasePort;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class GetUserByDocumentUseCase implements GetUserByDocumentUseCasePort {

    private final UserGateway userGateway;

    @Override
    public Mono<User> execute(String documentNumber) {
        return userGateway.findByDocumentNumber(documentNumber)
            .doOnNext(user -> {
                System.out.println("User encontrado: " + user.documentNumber() + ", " + user.name());
            })
            .switchIfEmpty(Mono.error(new UserNotFoundException(documentNumber)));
    }
}
