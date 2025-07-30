package lk.ijse.gdse71.back_end.exceptions;

//custom exception
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
