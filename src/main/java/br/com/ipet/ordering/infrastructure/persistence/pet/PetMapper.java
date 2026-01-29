package br.com.ipet.ordering.infrastructure.persistence.pet;

import br.com.ipet.ordering.domain.model.commons.CustomerId;
import br.com.ipet.ordering.domain.model.commons.PetId;
import br.com.ipet.ordering.domain.model.commons.PetName;
import br.com.ipet.ordering.domain.model.commons.Weight;
import br.com.ipet.ordering.domain.model.pet.Breed;
import br.com.ipet.ordering.domain.model.pet.Gender;
import br.com.ipet.ordering.domain.model.pet.Pet;
import br.com.ipet.ordering.domain.model.pet.Size;
import br.com.ipet.ordering.domain.model.pet.Type;
import org.springframework.stereotype.Component;

@Component
public class PetMapper {

    public Pet toDomain(PetPersistenceEntity petPersistence) {
        return Pet.existing()
                .id(new PetId(petPersistence.getId()))
                .customerId(new CustomerId(petPersistence.getCustomerId()))
                .name(new PetName(petPersistence.getName()))
                .type(Type.valueOf(petPersistence.getType()))
                .breed(Breed.valueOf(petPersistence.getBreed()))
                .size(Size.valueOf(petPersistence.getSize()))
                .gender(Gender.valueOf(petPersistence.getGender()))
                .weight(new Weight(petPersistence.getWeight()))
                .build();
    }

}
