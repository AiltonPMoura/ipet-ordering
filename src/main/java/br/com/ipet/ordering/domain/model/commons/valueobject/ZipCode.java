package br.com.ipet.ordering.domain.model.commons.valueobject;

import br.com.ipet.ordering.domain.model.FieldValidator;

public record ZipCode(Integer value) {

    public ZipCode {
        FieldValidator.requiresNonNull("zipCode value", value);
    }

}
