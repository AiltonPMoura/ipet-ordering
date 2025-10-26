package br.com.ipet.ordering.domain.valueobject;

import br.com.ipet.ordering.domain.util.FieldValidator;

public record CompanyName(String name) {

    public CompanyName {
        FieldValidator.requiresNonBlank("company name", name);
    }

}
