package br.com.ipet.ordering.domain.model.commons;

import br.com.ipet.ordering.domain.model.util.FieldValidator;

public record Cpf(String value) {

    public Cpf {
        FieldValidator.requiresNonNull("document value", value);
    }

}
