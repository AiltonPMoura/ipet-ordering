package br.com.ipet.ordering.domain.valueobject;

import br.com.ipet.ordering.domain.util.FieldValidator;

public record Product(
        String name,
        String description,
        Money price) {

    public Product {
        FieldValidator.requiresNonBlank("product name", name);
        FieldValidator.requiresNonBlank("product description", description);
        FieldValidator.requiresNonNull("product price", price);
    }

}
