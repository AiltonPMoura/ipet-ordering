package br.com.ipet.ordering.domain.model.valueobject;

import br.com.ipet.ordering.domain.model.util.FieldValidator;

public record Document(String value) {

    public Document {
        FieldValidator.requiresNonNull("document value", value);
    }

}
