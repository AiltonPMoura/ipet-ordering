package br.com.ipet.ordering.domain.model.commons;

import br.com.ipet.ordering.domain.model.util.FieldValidator;

public record PetName(String value) {

    public PetName {
        FieldValidator.requiresNonBlank("pet name value", value);
    }

}
