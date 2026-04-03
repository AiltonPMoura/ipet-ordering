package br.com.ipet.ordering.domain.model.pet;

import br.com.ipet.ordering.domain.model.Repository;
import br.com.ipet.ordering.domain.model.customer.CustomerId;

import java.util.Set;

public interface Pets extends Repository<Pet, PetId> {
    Set<Pet> ofCustomer(CustomerId customerId);
}
