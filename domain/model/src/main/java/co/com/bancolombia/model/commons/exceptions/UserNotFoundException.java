package co.com.bancolombia.model.commons.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String documentNumber) {
        super("No user with documentNumber was found: " + documentNumber);
    }
}
