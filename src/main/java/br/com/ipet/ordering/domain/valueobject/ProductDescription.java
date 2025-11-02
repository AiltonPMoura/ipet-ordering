package br.com.ipet.ordering.domain.valueobject;

import br.com.ipet.ordering.domain.exception.ProductDescriptionCannotBeVerySmall;
import br.com.ipet.ordering.domain.util.FieldValidator;

public record ProductDescription(String value) {

    public ProductDescription {
        FieldValidator.requiresNonBlank("product description", value);
        if (value.length() < 4)
            throw new ProductDescriptionCannotBeVerySmall();
    }

}
