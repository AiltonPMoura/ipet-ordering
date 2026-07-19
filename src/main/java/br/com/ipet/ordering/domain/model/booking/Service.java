package br.com.ipet.ordering.domain.model.booking;

import br.com.ipet.ordering.domain.model.FieldValidator;
import lombok.Builder;

import java.util.UUID;

@Builder
public record Service(
        UUID serviceId,
        String type,
        String category,
        String description,
        Integer duration,
        Double price) {

    public Service {
        FieldValidator.requiresNonNull("serviceId", serviceId);
        FieldValidator.requiresNonNull("type", type);
        FieldValidator.requiresNonNull("category", category);
        FieldValidator.requiresNonNull("description", description);
        FieldValidator.requiresNonNull("duration", duration);
        FieldValidator.requiresNonNull("price", price);
    }

}
