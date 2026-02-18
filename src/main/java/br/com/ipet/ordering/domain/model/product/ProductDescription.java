package br.com.ipet.ordering.domain.model.product;

import br.com.ipet.ordering.domain.model.FieldValidator;

public record ProductDescription(String value) {

    public ProductDescription {
        FieldValidator.requiresNonBlank("product description", value);

        //if (value.length() < 4)
            //throw new ProductDescriptionCannotBeVerySmallException();
    }

}
