package br.com.ipet.ordering.domain.model.company;

import br.com.ipet.ordering.domain.model.FieldValidator;

public record CompanyName(String value) {

    public CompanyName {
        FieldValidator.requiresNonBlank("company name", value);
    }

}
