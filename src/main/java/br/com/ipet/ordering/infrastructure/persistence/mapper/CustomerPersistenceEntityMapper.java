package br.com.ipet.ordering.infrastructure.persistence.mapper;

import br.com.ipet.ordering.domain.model.customer.Customer;
import br.com.ipet.ordering.domain.model.pet.Pet;
import br.com.ipet.ordering.infrastructure.persistence.entity.CustomerPersistenceEntity;
import br.com.ipet.ordering.infrastructure.persistence.entity.PetPersistenceEntity;
import br.com.ipet.ordering.infrastructure.persistence.repository.CustomerPersistenceEntityRepository;
import lombok.RequiredArgsConstructor;

import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CustomerPersistenceEntityMapper {

    private CustomerPersistenceEntityRepository customerPersistenceEntityRepository;

    public CustomerPersistenceEntity toPersistenceEntity(Customer customer) {
        return CustomerPersistenceEntity.builder()
                .id(customer.id().value())
                .pets(toPersistenceEntity(customer.pets()))
                .build();
    }

    private Set<PetPersistenceEntity> toPersistenceEntity(Set<Pet> pets) {
        return pets.stream().map(pet -> PetPersistenceEntity.builder()
                .id(pet.id().value())
                .customer(customerPersistenceEntityRepository.getReferenceById(pet.custumerId().value()))
                .build()
        ).collect(Collectors.toSet());
    }

}
