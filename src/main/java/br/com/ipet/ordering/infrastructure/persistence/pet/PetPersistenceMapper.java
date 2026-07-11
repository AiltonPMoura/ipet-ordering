package br.com.ipet.ordering.infrastructure.persistence.pet;

import br.com.ipet.ordering.domain.model.pet.Pet;
import br.com.ipet.ordering.infrastructure.persistence.customer.CustomerPersistenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PetPersistenceMapper {

    private final CustomerPersistenceRepository customerRepository;

    public PetPersistenceEntity fromDomain(Pet pet) {
        return this.merge(new PetPersistenceEntity(), pet);
    }

    public PetPersistenceEntity merge(PetPersistenceEntity petPersistence, Pet pet) {
        petPersistence.setId(pet.id().value());
        petPersistence.setCustomer(customerRepository.getReferenceById(pet.customerId().value()));
        petPersistence.setName(pet.name().value());
        petPersistence.setType(pet.type().name());
        petPersistence.setSize(pet.size().name());
        petPersistence.setBreed(pet.breed().name());
        petPersistence.setGender(pet.gender().name());
        petPersistence.setWeight(pet.weight().value());
        petPersistence.setAge(pet.age().value());
        return petPersistence;
    }

}
