package br.com.ipet.ordering.domain.model.scheduling;

import br.com.ipet.ordering.domain.model.FieldValidator;
import br.com.ipet.ordering.domain.model.commons.valueobject.ServiceId;
import br.com.ipet.ordering.domain.model.pet.PetSize;
import br.com.ipet.ordering.domain.model.schedule.category.ServiceCategory;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.Duration;

@Builder
public record Service(
        ServiceId serviceId,
        String description,
        ServiceCategory category,
        Duration duration,
        PetSize petSize,
        BigDecimal price) {

    public Service {
        FieldValidator.requiresNonNull("serviceId", serviceId);
        FieldValidator.requiresNonNull("service description", description);
        FieldValidator.requiresNonNull("service category", category);
        FieldValidator.requiresNonNull("service duration", duration);
        FieldValidator.requiresNonNull("service price", price);
    }

}
