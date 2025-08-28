package co.com.bancolombia.usecase.user;

import co.com.bancolombia.model.commons.exceptions.UserNotFoundException;
import co.com.bancolombia.model.commons.util.EmailUtils;
import co.com.bancolombia.model.user.User;
import co.com.bancolombia.model.role.gateways.RoleRepository;
import co.com.bancolombia.model.user.gateways.UserRepository;
import co.com.bancolombia.model.commons.exceptions.EmailAlreadyUsedException;
import co.com.bancolombia.model.commons.exceptions.RoleNotFoundException;
import co.com.bancolombia.usecase.user.input.UserUseCasePort;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UserUseCase implements UserUseCasePort {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public Mono<User> save(User user) {
        final String normalizedEmail = EmailUtils.normalizeEmail(user.getEmail());
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

    @Override
    public Mono<User> findByEmail(String email) {
        final String normalizedEmail = EmailUtils.normalizeEmail(email);
        return userRepository.findByEmail(normalizedEmail)
            .switchIfEmpty(Mono.error(new UserNotFoundException(email)));
    }
}
