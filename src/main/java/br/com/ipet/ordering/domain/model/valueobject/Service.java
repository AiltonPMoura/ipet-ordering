package br.com.ipet.ordering.domain.model.valueobject;

import br.com.ipet.ordering.domain.model.util.FieldValidator;

import java.time.OffsetDateTime;

public record Service(
        ServiceName name,
        ServiceDescription description,
        Money price,
        OffsetDateTime scheduling) {

    public Service {
        FieldValidator.requiresNonNull("product name", name);
        FieldValidator.requiresNonNull("product description", description);
        FieldValidator.requiresNonNull("product price", price);
    }

}
