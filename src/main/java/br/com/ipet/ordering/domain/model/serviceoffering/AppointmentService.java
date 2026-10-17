package br.com.ipet.ordering.domain.model.serviceoffering;

import br.com.ipet.ordering.domain.model.FieldValidator;
import br.com.ipet.ordering.domain.model.commons.valueobject.Money;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record AppointmentService(
        ServiceOfferingId id,
        String type,
        String category,
        String description,
        Integer duration,
        Money price,
        String petSize) {

    public AppointmentService {
        FieldValidator.requiresNonNull("id", id);
        FieldValidator.requiresNonNull("type", type);
        FieldValidator.requiresNonNull("category", category);
        FieldValidator.requiresNonNull("description", description);
        FieldValidator.requiresNonNull("duration", duration);
        FieldValidator.requiresNonNull("price", price);
        FieldValidator.requiresNonNull("petSize", petSize);
    }

}
