package br.com.ipet.ordering.domain.model.booking.stay;

import br.com.ipet.ordering.domain.model.FieldValidator;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
public record StayService(
        UUID serviceId,
        String type,
        String category,
        String description,
        BigDecimal price,
        String petSize) {

    public StayService {
        FieldValidator.requiresNonNull("serviceId", serviceId);
        FieldValidator.requiresNonNull("type", type);
        FieldValidator.requiresNonNull("category", category);
        FieldValidator.requiresNonNull("description", description);
        FieldValidator.requiresNonNull("price", price);
        FieldValidator.requiresNonNull("petSize", petSize);
    }

}
