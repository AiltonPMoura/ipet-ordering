package br.com.ipet.ordering.infrastructure.exception.handler;

import br.com.ipet.ordering.application.commons.NumberCannotBeNegativeException;
import br.com.ipet.ordering.domain.model.exception.*;
import br.com.ipet.ordering.domain.model.order.OrderCannotBePlacedException;
import br.com.ipet.ordering.domain.model.order.OrderIsNotDraftToChangeException;
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
                new ExceptionResponse(
                        ex.getMessage(),
                        request.getDescription(false),
                        OffsetDateTime.now()),
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

    @ExceptionHandler(OrderIsNotDraftToChangeException.class)
    public ResponseEntity<ExceptionResponse> orderIsNotDraftToChangeExceptionHandler(OrderIsNotDraftToChangeException ex, WebRequest request) {
        return new ResponseEntity<>(
                getMessageResponse(ex.getMessage(), request, ex.getOrderId()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AgendaIsNotDraftToChangeException.class)
    public ResponseEntity<ExceptionResponse> agendaIsNotDraftToChangeExceptionHandler(AgendaIsNotDraftToChangeException ex, WebRequest request) {
        return new ResponseEntity<>(
                getMessageResponse(ex.getMessage(), request, ex.getAgendaId()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SchedulingIsNotDraftToChangeException.class)
    public ResponseEntity<ExceptionResponse> schedulingIsNotDraftToChangeExceptionHandler(SchedulingIsNotDraftToChangeException ex, WebRequest request) {
        return new ResponseEntity<>(
                getMessageResponse(ex.getMessage(), request, ex.getSchedulingId()),
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

    @ExceptionHandler(ServiceNotFoundException.class)
    public ResponseEntity<ExceptionResponse> serviceNotFoundExceptionHandler(ServiceNotFoundException ex, WebRequest request) {
        return new ResponseEntity<>(
                getMessageResponse(ex.getMessage(), request, ex.getFields()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ExceptionResponse> productNotFoundExceptionHandler(ProductNotFoundException ex, WebRequest request) {
        return new ResponseEntity<>(
                getMessageResponse(ex.getMessage(), request, ex.getValue()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SchedulingItemNotFoundException.class)
    public ResponseEntity<ExceptionResponse> schedulingItemNotFoundExceptionHandler(SchedulingItemNotFoundException ex, WebRequest request) {
        return new ResponseEntity<>(
                getMessageResponse(ex.getMessage(), request, ex.getFields()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EndDateOrTimeMustBeLaterThanStartException.class)
    public ResponseEntity<ExceptionResponse> endDateOrTimeMustBeLaterThanStartHandler(EndDateOrTimeMustBeLaterThanStartException ex, WebRequest request) {
        return new ResponseEntity<>(
                getMessageResponse(ex.getMessage(), request, ex.getValues()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DateTimeMustBeLaterThanNowException.class)
    public ResponseEntity<ExceptionResponse> dateTimeMustBeLaterThanNowExceptionHandler(DateTimeMustBeLaterThanNowException ex,
                                                                                        WebRequest request) {
        return new ResponseEntity<>(
                getMessageResponse(ex.getMessage(), request, ex.getField()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CanNotChangeSchedulingAtException.class)
    public ResponseEntity<ExceptionResponse> canNotChangeSchedulingAtHandler(CanNotChangeSchedulingAtException ex, WebRequest request) {
        return new ResponseEntity<>(
                getMessageResponse(ex.getMessage(), request, ex.getValue()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(StockCannotBeNegativeException.class)
    public ResponseEntity<ExceptionResponse> stockCannotBeNegativeHandler(StockCannotBeNegativeException ex, WebRequest request) {
        return new ResponseEntity<>(
                getMessageResponse(ex.getMessage(), request, ""),
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
