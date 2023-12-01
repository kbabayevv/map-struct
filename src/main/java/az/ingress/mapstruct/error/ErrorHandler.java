package az.ingress.mapstruct.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(value = UserNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponse handle(UserNotFound userNotFound) {
        return new ExceptionResponse(userNotFound.getMessage());
    }
}
