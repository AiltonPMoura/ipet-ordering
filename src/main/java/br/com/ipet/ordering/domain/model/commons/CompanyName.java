package br.com.ipet.ordering.domain.model.commons;

import br.com.ipet.ordering.domain.model.util.FieldValidator;

public record CompanyName(String value) {

    public CompanyName {
        FieldValidator.requiresNonBlank("company name", value);
    }

}
