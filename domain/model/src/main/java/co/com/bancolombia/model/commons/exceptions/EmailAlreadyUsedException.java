package co.com.bancolombia.model.commons.exceptions;

public class EmailAlreadyUsedException extends RuntimeException {
    public EmailAlreadyUsedException(String email) {
        super("email already registered: " + email);
    }
}
