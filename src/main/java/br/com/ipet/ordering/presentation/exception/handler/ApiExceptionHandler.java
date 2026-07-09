package br.com.ipet.ordering.presentation.exception.handler;

import br.com.ipet.ordering.domain.model.DomainEntityNotFoundException;
import br.com.ipet.ordering.domain.model.DomainException;
import br.com.ipet.ordering.domain.model.customer.CustomerEmailIsInUseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;
import java.util.Locale;
import java.util.stream.Collectors;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    private final MessageSource messageSource;

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {

        var fieldErrors = ex.getBindingResult().getAllErrors().stream().collect(
                Collectors.toMap(
                        objectError -> ((FieldError) objectError).getField(),
                        objectError -> messageSource.getMessage(objectError, LocaleContextHolder.getLocale())
                )
        );

        var problemDetail = buildProblemDetail(
                status,
                "Invalid Fields",
                "/errors/invalid-fields",
                "invalid.fields"
        );

        problemDetail.setProperty("fields", fieldErrors);

        return super.handleExceptionInternal(ex, problemDetail, headers, status, request);
    }

    @ExceptionHandler(DomainException.class)
    public ProblemDetail handleDomainException(DomainException ex) {
        return switch (ex) {
            case DomainEntityNotFoundException notFound -> buildProblemDetail(HttpStatus.NOT_FOUND,
                    "Not Found",
                    "/errors/not-found",
                    notFound.getMessage(),
                    notFound.getValues()
            );
            case CustomerEmailIsInUseException emailsNotUnique -> buildProblemDetail(HttpStatus.CONFLICT,
                    "Conflict",
                    "/errors/conflict",
                    emailsNotUnique.getMessage(),
                    emailsNotUnique.getValues()
            );
            default -> buildProblemDetail(HttpStatus.UNPROCESSABLE_ENTITY,
                    "Unprocessable Entity",
                    "/errors/unprocessable-entity",
                    ex.getMessage(),
                    ex.getValues()
            );
        };

    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleException(Exception ex) {
        log.error(ex.getMessage(), ex);
        return buildProblemDetail(HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal Server Error",
                "/errors/internal",
                "internal.error");
    }

    private ProblemDetail buildProblemDetail(HttpStatusCode status, String title, String uri, String messageKey, String... params) {
        var problemDetail = ProblemDetail.forStatus(status);
        problemDetail.setTitle(title);
        problemDetail.setType(URI.create(uri));
        problemDetail.setDetail(messageSource.getMessage(messageKey, params, LocaleContextHolder.getLocale()));
        return problemDetail;
    }

}
