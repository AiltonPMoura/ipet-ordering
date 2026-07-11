package br.com.ipet.ordering.domain.model.booking;

import br.com.ipet.ordering.domain.model.FieldValidator;
import br.com.ipet.ordering.domain.model.pet.Breed;
import br.com.ipet.ordering.domain.model.pet.PetGender;
import br.com.ipet.ordering.domain.model.pet.PetId;
import br.com.ipet.ordering.domain.model.pet.PetName;
import br.com.ipet.ordering.domain.model.pet.PetSize;
import br.com.ipet.ordering.domain.model.pet.PetType;

public record Pet(PetId id, PetName name, PetType type, Breed breed,
                  PetGender petGender, PetSize size, Double weight, Integer age) {

    public Pet {
        FieldValidator.requiresNonNull("pet id", id);
        FieldValidator.requiresNonNull("pet name", name);
        FieldValidator.requiresNonNull("pet type", type);
        FieldValidator.requiresNonNull("pet size", size);
    }

}