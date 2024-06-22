package challenge.demo.Services.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class DataIntegrityViolationException extends RuntimeException {

    public DataIntegrityViolationException() {
        super();
    }

    public DataIntegrityViolationException(String message) {
        super(message);
    }
}
