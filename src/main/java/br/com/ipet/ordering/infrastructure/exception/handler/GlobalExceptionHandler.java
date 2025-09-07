package br.com.ipet.ordering.infrastructure.exception.handler;

import br.com.ipet.ordering.domain.exception.FieldCannotBeEmptyException;
import br.com.ipet.ordering.domain.exception.EmailValidatorException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Locale;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(Exception.class)
    public String globalExceptionHandler(Exception ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(FieldCannotBeEmptyException.class)
    public String globalExceptionHandler(FieldCannotBeEmptyException ex) {
        return messageSource.getMessage(ex.getMessage(), new String[]{ex.getField()}, Locale.getDefault());
    }

    @ExceptionHandler(EmailValidatorException.class)
    public String globalExceptionHandler(EmailValidatorException ex) {
        return messageSource.getMessage(ex.getMessage(), new String[]{ex.getEmail()}, Locale.getDefault());
    }
}
