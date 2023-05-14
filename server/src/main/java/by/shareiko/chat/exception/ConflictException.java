package by.shareiko.chat.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public abstract class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }

}
