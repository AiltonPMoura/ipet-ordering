package br.com.ipet.ordering.domain.valueobject;

import br.com.ipet.ordering.domain.util.FieldValidator;

public record Description(String value) {

    public Description {
        FieldValidator.requiresNonBlank("description", value);
    }

}
