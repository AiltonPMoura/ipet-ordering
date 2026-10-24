package br.com.ipet.ordering.domain.model.commons.document;

import br.com.ipet.ordering.domain.model.FieldValidator;

public record Cnh(String value) implements Document {

    public Cnh {
        FieldValidator.requiresNonBlank("cnh", value);

        if (!value.matches("\\d{11}"))
            throw new DocumentIsNotValidException(value);
    }

}
