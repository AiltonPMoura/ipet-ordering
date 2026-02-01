package br.com.ipet.ordering.domain.model.customer;

import br.com.ipet.ordering.domain.model.FieldValidator;

public record CompanyName(String value) {

    public CompanyName {
        FieldValidator.requiresNonBlank("company name", value);
    }

}
