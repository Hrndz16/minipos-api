package epw.minipos_api.exception;

public class InvalidRoleException extends RuntimeException {
    public InvalidRoleException(long roleId) {
        super("Role with id '" + roleId + "' is not supported");
    }
}
