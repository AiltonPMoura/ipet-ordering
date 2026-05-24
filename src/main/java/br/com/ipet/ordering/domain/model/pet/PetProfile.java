package br.com.ipet.ordering.domain.model.pet;

import br.com.ipet.ordering.domain.model.FieldValidator;
import lombok.Builder;

@Builder
public record PetProfile(PetName name,
                         PetType petType, Breed breed, PetGender gender,
                         PetSize size, PetWeight weight, PetAge age) {

    public PetProfile {
        FieldValidator.requiresNonNull("name", name);
        FieldValidator.requiresNonNull("type", petType);
        FieldValidator.requiresNonNull("breed", breed);
        FieldValidator.requiresNonNull("gender", gender);
        FieldValidator.requiresNonNull("size", size);
        FieldValidator.requiresNonNull("weight", weight);
        FieldValidator.requiresNonNull("age", age);
    }

}
