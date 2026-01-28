package br.com.ipet.ordering.infrastructure.persistence.mapper;

import br.com.ipet.ordering.domain.model.customer.Customer;
import br.com.ipet.ordering.domain.model.pet.Pet;
import br.com.ipet.ordering.infrastructure.persistence.entity.CustomerPersistenceEntity;
import br.com.ipet.ordering.infrastructure.persistence.entity.PetPersistenceEntity;
import br.com.ipet.ordering.infrastructure.persistence.repository.CustomerPersistenceRepository;
import lombok.RequiredArgsConstructor;

import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CustomerPersistenceMapper {

    private final CustomerPersistenceRepository customerPersistenceRepository;

    public CustomerPersistenceEntity toPersistence(Customer customer) {
        return CustomerPersistenceEntity.builder()
                .id(customer.id().value())
                .pets(toPersistence(customer.pets()))
                .build();
    }

    private Set<PetPersistenceEntity> toPersistence(Set<Pet> pets) {
        return pets.stream().map(pet -> PetPersistenceEntity.builder()
                .id(pet.id().value())
                .customer(customerPersistenceRepository.getReferenceById(pet.custumerId().value()))
                .build()
        ).collect(Collectors.toSet());
    }

}
