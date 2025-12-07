package br.com.ipet.ordering.domain.model.valueobject;

import br.com.ipet.ordering.domain.model.util.FieldValidator;

public record ZipCode(Integer value) {

    public ZipCode {
        FieldValidator.requiresNonNull("zipCode value", value);
    }

}
