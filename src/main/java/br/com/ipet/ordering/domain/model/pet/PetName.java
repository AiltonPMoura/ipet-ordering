package br.com.ipet.ordering.domain.model.pet;

import br.com.ipet.ordering.domain.model.FieldValidator;

public record PetName(String value) {

    public PetName {
        FieldValidator.requiresNonBlank("pet name value", value);
    }

}
