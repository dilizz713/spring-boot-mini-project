package lk.ijse.gdse71.back_end.exceptions;

import lk.ijse.gdse71.back_end.util.APIResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)    // Exception - root class
    public ResponseEntity<APIResponse> handleGenericException(Exception e){
        return new ResponseEntity<>(new APIResponse(500,e.getMessage(),null), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ResourceNotFoundException.class)    //ResourceNotFoundException - custom exception
    public ResponseEntity<APIResponse> resourceNotFound(ResourceNotFoundException e){
        return new ResponseEntity<>(new APIResponse(404,e.getMessage(),null), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<APIResponse> resourceAlreadyExist(ResourceAlreadyExistsException e){
        return new ResponseEntity<>(new APIResponse(409,e.getMessage(),null), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<APIResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException e){
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(fieldError -> {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        });
        return new ResponseEntity<>(new APIResponse(400,"Validation failed",errors), HttpStatus.BAD_REQUEST);
    }
}
