package co.com.bancolombia.usecase.user.exception;

public class EmailAlreadyUsedException extends RuntimeException {
    public EmailAlreadyUsedException(String email) {
        super("email already registered: " + email);
    }
}
