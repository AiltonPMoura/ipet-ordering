package br.com.ipet.ordering.domain.valueobject;

import br.com.ipet.ordering.domain.util.FieldValidator;

public record PetName(String value) {

    public PetName {
        FieldValidator.requiresNonBlank("pet name value", value);
    }

}
