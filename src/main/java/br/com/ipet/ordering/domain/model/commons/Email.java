package br.com.ipet.ordering.domain.model.commons;

import br.com.ipet.ordering.domain.model.FieldValidator;

public record Email(String value) {

    public Email {
        FieldValidator.requiresNonBlank("email value", value);
        FieldValidator.emailValidator(value);
    }

}
