package br.com.ipet.ordering.domain.util;

import br.com.ipet.ordering.domain.exception.EmailValidatorException;
import br.com.ipet.ordering.domain.exception.FieldCannotBeEmptyException;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.util.StringUtils;

public class FieldValidator {

    private FieldValidator(){}

    public static void notNull(String field, Object value) {
        if (value == null)
            throw new FieldCannotBeEmptyException(field);
    }

    public static void notBlank(String field, String value) {
        if (!StringUtils.hasText(value))
            throw new FieldCannotBeEmptyException(field);
    }

    public static void emailValidator(String email) {
        if (!EmailValidator.getInstance().isValid(email))
            throw new EmailValidatorException(email);
    }

}
