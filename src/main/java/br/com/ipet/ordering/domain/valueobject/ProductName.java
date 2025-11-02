package br.com.ipet.ordering.domain.valueobject;

import br.com.ipet.ordering.domain.exception.ProductNameCannotBeVerySmall;
import br.com.ipet.ordering.domain.util.FieldValidator;

public record ProductName(String value) {

    public ProductName {
        FieldValidator.requiresNonBlank("product name", value);
        if (value.length() < 4)
            throw new ProductNameCannotBeVerySmall();
    }

}
