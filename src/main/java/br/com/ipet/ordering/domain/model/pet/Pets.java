package br.com.ipet.ordering.domain.model.pet;

import br.com.ipet.ordering.domain.model.RemoveCapableRepository;
import br.com.ipet.ordering.domain.model.commons.id.CustomerId;
import br.com.ipet.ordering.domain.model.commons.id.PetId;

import java.util.Set;

public interface Pets extends RemoveCapableRepository<Pet, PetId> {
    Set<Pet> ofCustomer(CustomerId customerId);
    Set<Pet> ofCustomerAndIds(CustomerId customerId, Set<PetId> petIds);
}
