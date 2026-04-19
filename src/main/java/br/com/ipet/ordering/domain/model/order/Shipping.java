package br.com.ipet.ordering.domain.model.order;

import br.com.ipet.ordering.domain.model.FieldValidator;
import br.com.ipet.ordering.domain.model.commons.valueobject.Address;
import br.com.ipet.ordering.domain.model.commons.valueobject.Money;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record Shipping(Money cost, LocalDate expectedDate, Address address) {

    public Shipping {
        FieldValidator.requiresNonNull("shippingCost", cost);
        FieldValidator.requiresNonNull("expectedDate", expectedDate);
        FieldValidator.requiresNonNull("address", address);
    }

}
