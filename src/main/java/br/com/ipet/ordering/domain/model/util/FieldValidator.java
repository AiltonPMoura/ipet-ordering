package br.com.ipet.ordering.domain.model.util;

import br.com.ipet.ordering.domain.model.exception.DateMustBeLaterThanNowException;
import br.com.ipet.ordering.domain.model.exception.DateTimeMustBeLaterThanNowException;
import br.com.ipet.ordering.domain.model.exception.EmailValidatorException;
import br.com.ipet.ordering.domain.model.exception.FieldCannotBeEmptyException;
import br.com.ipet.ordering.domain.model.exception.EndDateOrTimeMustBeLaterThanStartException;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.util.Collection;

public class FieldValidator {

    private FieldValidator(){}

    public static void requiresNonNull(String field, Object value) {
        if (value == null)
            throw new FieldCannotBeEmptyException(field);
    }

    public static void requiresNonBlank(String field, String value) {
        if (!StringUtils.hasText(value))
            throw new FieldCannotBeEmptyException(field);
    }

    public static void requiresNonEmpty(String field, Collection<?> value) {
        requiresNonNull(field, value);

        if (value.isEmpty())
            throw new FieldCannotBeEmptyException(field);
    }

    public static void emailValidator(String email) {
        if (!EmailValidator.getInstance().isValid(email))
            throw new EmailValidatorException(email);
    }

    public static void requireDateTimeIsAfterNow(String field, OffsetDateTime dateTime) {
        if (!dateTime.isAfter(OffsetDateTime.now()))
            throw new DateTimeMustBeLaterThanNowException(field);
    }

    public static void requireDateIsAfterNow(String field, LocalDate date) {
        if (!date.isAfter(LocalDate.now()))
            throw new DateMustBeLaterThanNowException(field);
    }

    public static void requireEndTimeIsAfterStartTime(OffsetTime startTime, OffsetTime endTime) {
        if (!endTime.isAfter(startTime))
            throw new EndDateOrTimeMustBeLaterThanStartException(startTime.toString(), endTime.toString());
    }

    public static void requireEndDateIsAfterStartDate(LocalDate startDate, LocalDate endDate) {
        if (!endDate.isAfter(startDate))
            throw new EndDateOrTimeMustBeLaterThanStartException(startDate.toString(), endDate.toString());
    }
}
