package br.com.ipet.ordering.domain.valueobject;

import br.com.ipet.ordering.domain.util.FieldValidator;

public record Cnpj(String value) {

    public Cnpj {
        FieldValidator.requiresNonBlank("cnpj value", value);
    }

}
