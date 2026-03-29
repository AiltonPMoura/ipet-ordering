package br.com.ipet.ordering.domain.model.company;

import br.com.ipet.ordering.domain.model.FieldValidator;

public record Cnpj(String value) {

    public Cnpj {
        FieldValidator.requiresNonBlank("cnpj value", value);
    }

}
