package br.com.ipet.ordering.domain.model.commons;

import br.com.ipet.ordering.domain.model.util.FieldValidator;

public record Cnpj(String value) {

    public Cnpj {
        FieldValidator.requiresNonBlank("cnpj value", value);
    }

}
