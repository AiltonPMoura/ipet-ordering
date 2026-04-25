package br.com.ipet.ordering.domain.model.pet;

import br.com.ipet.ordering.domain.model.FieldValidator;
import lombok.Builder;

@Builder
public record PetProfile(PetName name,
                         Type type, Breed breed, Gender gender,
                         Size size, PetWeight weight, PetAge age) {

    public PetProfile {
        FieldValidator.requiresNonNull("name", name);
        FieldValidator.requiresNonNull("type", type);
        FieldValidator.requiresNonNull("breed", breed);
        FieldValidator.requiresNonNull("gender", gender);
        FieldValidator.requiresNonNull("size", size);
        FieldValidator.requiresNonNull("weight", weight);
        FieldValidator.requiresNonNull("age", age);
    }

}
