package co.com.bancolombia.usecase.user;

import co.com.bancolombia.model.user.User;
import co.com.bancolombia.model.role.gateways.RoleRepository;
import co.com.bancolombia.model.user.gateways.UserRepository;
import co.com.bancolombia.usecase.user.exception.EmailAlreadyUsedException;
import co.com.bancolombia.usecase.user.exception.RoleNotFoundException;
import co.com.bancolombia.usecase.user.input.UserUseCasePort;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.Locale;

@RequiredArgsConstructor
public class UserUseCase implements UserUseCasePort {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public Mono<User> saveUser(User user) {
        final String normalizedEmail = user.getEmail().strip().toLowerCase(Locale.ROOT);
        final String roleId = user.getRoleId();

        Mono<Void> validateEmailNotUsed =
            userRepository.existsByEmail(normalizedEmail)
            .flatMap(exists -> exists
                ? Mono.error(new EmailAlreadyUsedException(user.getEmail()))
                : Mono.empty());

        Mono<Void> validateRoleExists =
            roleRepository.existsById(roleId)
            .flatMap(exists -> exists
                ? Mono.empty()
                : Mono.error(new RoleNotFoundException(roleId)));

        return validateEmailNotUsed
            .then(validateRoleExists)
            .then(userRepository.save(user.withEmail(normalizedEmail)));
    }
}
