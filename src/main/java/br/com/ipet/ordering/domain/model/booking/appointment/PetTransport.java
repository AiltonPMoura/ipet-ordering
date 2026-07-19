package br.com.ipet.ordering.domain.model.booking.appointment;

import br.com.ipet.ordering.domain.model.commons.valueobject.Address;
import br.com.ipet.ordering.domain.model.commons.valueobject.Company;
import br.com.ipet.ordering.domain.model.commons.valueobject.Money;
import lombok.Builder;

import static br.com.ipet.ordering.domain.model.FieldValidator.requiresNonNull;

@Builder
public record PetTransport(Money cost, Address address, Company company) {

     public PetTransport {
         requiresNonNull("cost", cost);
         requiresNonNull("address", address);
         requiresNonNull("company", company);
     }

}
