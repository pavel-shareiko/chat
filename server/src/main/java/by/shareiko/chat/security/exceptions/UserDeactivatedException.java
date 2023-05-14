package by.shareiko.chat.security.exceptions;


import org.springframework.security.core.AuthenticationException;

public class UserDeactivatedException extends AuthenticationException {

    public UserDeactivatedException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public UserDeactivatedException(String msg) {
        super(msg);
    }
}
