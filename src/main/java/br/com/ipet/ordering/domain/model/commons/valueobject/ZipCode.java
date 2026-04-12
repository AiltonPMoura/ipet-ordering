package br.com.ipet.ordering.domain.model.commons.valueobject;

import br.com.ipet.ordering.domain.model.FieldValidator;

public record ZipCode(String value) {

    public ZipCode {
        FieldValidator.requiresNonNull("zipCode value", value);
    }

}
