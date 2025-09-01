package co.com.bancolombia.model.commons.exceptions;

public class RoleNotFoundException extends RuntimeException {
    public RoleNotFoundException(String roleId) {
        super("roleId not found: " + roleId);
    }
}
