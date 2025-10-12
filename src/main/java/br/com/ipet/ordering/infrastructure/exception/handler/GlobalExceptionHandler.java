package br.com.ipet.ordering.infrastructure.exception.handler;

import br.com.ipet.ordering.domain.exception.*;
import br.com.ipet.ordering.infrastructure.exception.response.ExceptionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.OffsetDateTime;
import java.util.Locale;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> globalExceptionHandler(Exception ex, WebRequest request) {
        return new ResponseEntity<>(
                getMessageResponse(ex.getMessage(), request, ""),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(FieldCannotBeEmptyException.class)
    public ResponseEntity<ExceptionResponse> fieldCannotBeEmptyExceptionHandler(FieldCannotBeEmptyException ex, WebRequest request) {
        return new ResponseEntity<>(
                getMessageResponse(ex.getMessage(), request, ex.getField()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmailValidatorException.class)
    public ResponseEntity<ExceptionResponse> emailValidatorExceptionHandler(EmailValidatorException ex, WebRequest request) {
        return new ResponseEntity<>(
                getMessageResponse(ex.getMessage(), request, ex.getEmail()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(OrderCannotBePlacedException.class)
    public ResponseEntity<ExceptionResponse> orderCannotBePlacedExceptionHandler(OrderCannotBePlacedException ex, WebRequest request) {
        return new ResponseEntity<>(
                getMessageResponse(ex.getMessage(), request, ex.getValue()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NumberCannotBeNegativeException.class)
    public ResponseEntity<ExceptionResponse> numberCannotBeNegativeExceptionHandler(NumberCannotBeNegativeException ex, WebRequest request) {
        return new ResponseEntity<>(
                getMessageResponse(ex.getMessage(), request, ex.getField()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(QuantityGreaterThanZeroException.class)
    public ResponseEntity<ExceptionResponse> quantityGreaterThanZeroExceptionHandler(QuantityGreaterThanZeroException ex, WebRequest request) {
        return new ResponseEntity<>(
                getMessageResponse(ex.getMessage(), request, ""),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CannotBeChangeStatusException.class)
    public ResponseEntity<ExceptionResponse> cannotBeChangeStatusExceptionHandler(CannotBeChangeStatusException ex, WebRequest request) {
        return new ResponseEntity<>(
                getMessageResponse(ex.getMessage(), request, ex.getStatus()),
                HttpStatus.BAD_REQUEST);
    }

    private ExceptionResponse getMessageResponse(String message, WebRequest request, String... params) {
        return new ExceptionResponse(
                messageSource.getMessage(message, params, Locale.getDefault()),
                request.getDescription(false),
                OffsetDateTime.now()
        );
    }

}
