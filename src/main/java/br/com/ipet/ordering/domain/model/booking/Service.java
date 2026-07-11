package br.com.ipet.ordering.domain.model.booking;

import br.com.ipet.ordering.domain.model.FieldValidator;
import br.com.ipet.ordering.domain.model.commons.valueobject.Money;
import br.com.ipet.ordering.domain.model.pet.PetSize;
import br.com.ipet.ordering.domain.model.schedule.category.ServiceCategory;
import lombok.Builder;

import java.time.Duration;
import java.util.UUID;

@Builder
public record Service(
        UUID serviceId,
        String description,
        ServiceCategory category,
        Duration duration,
        PetSize petSize,
        Money price) {

    public Service {
        FieldValidator.requiresNonNull("serviceId", serviceId);
        FieldValidator.requiresNonNull("service description", description);
        FieldValidator.requiresNonNull("service category", category);
        FieldValidator.requiresNonNull("service duration", duration);
        FieldValidator.requiresNonNull("service price", price);
    }

}
