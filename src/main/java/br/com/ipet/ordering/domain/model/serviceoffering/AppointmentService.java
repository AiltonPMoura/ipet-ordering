package br.com.ipet.ordering.domain.model.serviceoffering;

import br.com.ipet.ordering.domain.model.FieldValidator;
import br.com.ipet.ordering.domain.model.commons.valueobject.Money;
import br.com.ipet.ordering.domain.model.commons.enums.PetSize;
import lombok.Builder;

@Builder
public record AppointmentService(
        ServiceId id,
        PetSize petSize,
        Money price,
        String type,
        ServiceCategory category,
        String description,
        Integer duration) {

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
