package br.com.ipet.ordering.domain.model.product;

import br.com.ipet.ordering.domain.model.FieldValidator;
import br.com.ipet.ordering.domain.model.commons.valueobject.Money;
import lombok.Builder;

@Builder
public record Product(
        ProductId id,
        ProductName name,
        ProductDescription description,
        Money price) {

    public Product {
        FieldValidator.requiresNonNull("product id", id);
        FieldValidator.requiresNonNull("product name", name);
        FieldValidator.requiresNonNull("product price", price);
    }

}
