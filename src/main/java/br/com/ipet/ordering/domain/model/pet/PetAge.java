package br.com.ipet.ordering.domain.model.pet;

import br.com.ipet.ordering.domain.model.FieldValidator;

public record PetAge(Integer value) {

    public PetAge {
        FieldValidator.requiresNonNull("age", value);
        if (value < 0) throw new AgeCannotBeNegativeException();
    }

}
