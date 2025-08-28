package co.com.bancolombia.model.commons.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String email) {
        super("No user with email was found: " + email);
    }
}
