package br.com.ipet.ordering.domain.model.valueobject;

import br.com.ipet.ordering.domain.model.util.FieldValidator;

public record CompanyName(String name) {

    public CompanyName {
        FieldValidator.requiresNonBlank("company name", name);
    }

}
