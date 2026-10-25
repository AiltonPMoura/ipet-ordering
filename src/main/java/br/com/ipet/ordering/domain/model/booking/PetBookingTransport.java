package br.com.ipet.ordering.domain.model.booking;

import br.com.ipet.ordering.domain.model.commons.valueobject.Address;
import br.com.ipet.ordering.domain.model.commons.valueobject.Money;
import lombok.Builder;

import static br.com.ipet.ordering.domain.model.FieldValidator.requiresNonNull;

@Builder
public record PetBookingTransport(Money cost, Address address) {

     public PetBookingTransport {
         requiresNonNull("cost", cost);
         requiresNonNull("address", address);
     }

}
