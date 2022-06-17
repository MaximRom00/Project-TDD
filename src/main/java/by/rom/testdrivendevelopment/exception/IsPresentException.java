package by.rom.testdrivendevelopment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class IsPresentException extends RuntimeException{

    public IsPresentException(String message) {
        super(message);
    }
}
