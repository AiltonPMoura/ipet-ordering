package br.com.ipet.ordering.domain.model.pet;

import br.com.ipet.ordering.domain.model.FieldValidator;

public record PetWeight(Double value) {

    public PetWeight {
        FieldValidator.requiresNonNull("pet weight", value);
        if (value < 0) throw new PetWeightCannoeBeNegativeException();
    }

}
