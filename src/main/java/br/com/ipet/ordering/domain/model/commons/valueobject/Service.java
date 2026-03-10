package br.com.ipet.ordering.domain.model.commons.valueobject;

import br.com.ipet.ordering.domain.model.FieldValidator;
import br.com.ipet.ordering.domain.model.pet.Size;
import br.com.ipet.ordering.domain.model.scheduling.PriceCannotIsEmpty;
import lombok.Builder;

import java.util.Map;

@Builder
public record Service(
        ServiceId serviceId,
        String type,
        String size,
        ServiceDescription description,
        Money price) {

    public Service {
        FieldValidator.requiresNonNull("serviceId", serviceId);
        FieldValidator.requiresNonNull("service type", type);
        FieldValidator.requiresNonNull("service price", price);
    }

}
