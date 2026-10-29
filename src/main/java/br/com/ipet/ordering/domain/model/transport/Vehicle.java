package br.com.ipet.ordering.domain.model.transport;

import br.com.ipet.ordering.domain.model.FieldValidator;
import lombok.Builder;

@Builder
public record Vehicle(LicensePlate licensePlate, String brand, String model) {

    public Vehicle {
        FieldValidator.requiresNonNull("licensePlate", licensePlate);
        FieldValidator.requiresNonNull("brand", brand);
        FieldValidator.requiresNonNull("model", model);
    }

}
