package br.com.ipet.ordering.domain.model.customer;

import br.com.ipet.ordering.domain.model.FieldValidator;

public record Cpf(String value) {

    public Cpf {
        FieldValidator.requiresNonNull("document value", value);
    }

}
