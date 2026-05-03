package br.com.ipet.ordering.domain.model.commons.document;

import br.com.ipet.ordering.domain.model.FieldValidator;

public record Cpf(String value) implements Document {

    public Cpf {
        FieldValidator.requiresNonBlank("cpf", value);

        if (!value.matches("\\d{11}"))
            throw new DocumentIsNotValidException("cpf inválido");
    }

}
