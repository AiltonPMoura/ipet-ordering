package br.com.ipet.ordering.domain.model.pet;

import br.com.ipet.ordering.domain.model.Repository;
import br.com.ipet.ordering.domain.model.customer.CustomerId;

import java.util.Optional;

public interface Pets extends Repository<Pet, PetId> {
    Optional<Pet> ofCustomer(PetId id, CustomerId customerId);
}
