package epw.minipos_api.exception;

public class CustomerNotFoundException extends RuntimeException {
    public CustomerNotFoundException(long id) {
        super("Customer with id " + id + " was not found");
    }
}
