package br.com.ipet.ordering.domain.valueobject;

import br.com.ipet.ordering.domain.util.FieldValidator;

public record ZipCode(Integer value) {

    public ZipCode {
        FieldValidator.notNull("zipCode value", value);
    }

}
