package br.com.ipet.ordering.domain.model.pet;

import br.com.ipet.ordering.domain.model.FieldValidator;

public record PetAge(Integer age) {

    public PetAge {
        FieldValidator.requiresNonNull("pet age", age);
        if (age < 0) throw new AgeCannotBeNegativeException();
    }

}
