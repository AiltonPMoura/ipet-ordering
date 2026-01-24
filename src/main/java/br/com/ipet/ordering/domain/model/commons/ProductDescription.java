package br.com.ipet.ordering.domain.model.commons;

import br.com.ipet.ordering.domain.model.exception.ProductDescriptionCannotBeVerySmallException;
import br.com.ipet.ordering.domain.model.util.FieldValidator;

public record ProductDescription(String value) {

    public ProductDescription {
        FieldValidator.requiresNonBlank("product description", value);

        if (value.length() < 4)
            throw new ProductDescriptionCannotBeVerySmallException();
    }

}
