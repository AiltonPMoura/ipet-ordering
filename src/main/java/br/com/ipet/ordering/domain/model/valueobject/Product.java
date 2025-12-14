package br.com.ipet.ordering.domain.model.valueobject;

import br.com.ipet.ordering.domain.model.util.FieldValidator;
import lombok.Builder;

@Builder
public record Product(
        ProductName name,
        ProductDescription description,
        Money price) {

    public Product {
        FieldValidator.requiresNonNull("product name", name);
        FieldValidator.requiresNonNull("product description", description);
        FieldValidator.requiresNonNull("product price", price);
    }

}
