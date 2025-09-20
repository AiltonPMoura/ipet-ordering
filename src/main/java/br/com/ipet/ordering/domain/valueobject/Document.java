package br.com.ipet.ordering.domain.valueobject;

import br.com.ipet.ordering.domain.util.FieldValidator;

public record Document(Integer value) {

    public Document {
        FieldValidator.requiresNonNull("document value", value);
    }

}
