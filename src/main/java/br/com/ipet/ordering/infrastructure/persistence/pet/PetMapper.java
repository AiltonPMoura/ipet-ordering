package br.com.ipet.ordering.infrastructure.persistence.pet;

import br.com.ipet.ordering.domain.model.customer.CustomerId;
import br.com.ipet.ordering.domain.model.pet.PetAge;
import br.com.ipet.ordering.domain.model.pet.PetId;
import br.com.ipet.ordering.domain.model.pet.PetName;
import br.com.ipet.ordering.domain.model.pet.PetType;
import br.com.ipet.ordering.domain.model.pet.PetWeight;
import br.com.ipet.ordering.domain.model.pet.Breed;
import br.com.ipet.ordering.domain.model.pet.PetGender;
import br.com.ipet.ordering.domain.model.pet.Pet;
import br.com.ipet.ordering.domain.model.pet.PetSize;
import org.springframework.stereotype.Component;

@Component
public class PetMapper {

    public Pet toDomain(PetPersistenceEntity petPersistence) {
        return Pet.existing()
                .id(new PetId(petPersistence.getId()))
                .customerId(new CustomerId(petPersistence.getCustomerId()))
                .name(new PetName(petPersistence.getName()))
                .petType(PetType.valueOf(petPersistence.getType()))
                .breed(Breed.valueOf(petPersistence.getBreed()))
                .size(PetSize.valueOf(petPersistence.getSize()))
                .gender(PetGender.valueOf(petPersistence.getGender()))
                .weight(new PetWeight(petPersistence.getWeight()))
                .age(new PetAge(petPersistence.getAge()))
                .build();
    }

}
