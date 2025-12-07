package br.com.ipet.ordering.domain.model.valueobject;

import br.com.ipet.ordering.domain.model.exception.ProductNameCannotBeVerySmall;
import br.com.ipet.ordering.domain.model.util.FieldValidator;

public record ProductName(String value) {

    public ProductName {
        FieldValidator.requiresNonBlank("product name", value);
        if (value.length() < 4)
            throw new ProductNameCannotBeVerySmall();
    }

}
