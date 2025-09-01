package co.com.bancolombia.usecase.user.exception;

public class RoleNotFoundException extends RuntimeException {
    public RoleNotFoundException(String roleId) {
        super("roleId not found: " + roleId);
    }
}
