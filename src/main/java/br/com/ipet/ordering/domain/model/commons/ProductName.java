package br.com.ipet.ordering.domain.model.commons;

import br.com.ipet.ordering.domain.model.FieldValidator;
import br.com.ipet.ordering.domain.model.exception.ProductNameCannotBeVerySmall;

public record ProductName(String value) {

    public ProductName {
        FieldValidator.requiresNonBlank("product name", value);

        if (value.length() < 4)
            throw new ProductNameCannotBeVerySmall();
    }

}
