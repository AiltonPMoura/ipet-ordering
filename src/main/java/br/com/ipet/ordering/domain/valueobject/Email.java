package br.com.ipet.ordering.domain.valueobject;

import br.com.ipet.ordering.domain.util.FieldValidator;

public record Email(String value) {

    public Email {
        FieldValidator.requiresNonBlank("email value", value);
        FieldValidator.emailValidator(value);
    }

}
