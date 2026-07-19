package br.com.ipet.ordering.domain.model.booking;

import br.com.ipet.ordering.domain.model.FieldValidator;

import java.util.UUID;

public record Pet(
        UUID id,
        String name,
        String type,
        String breed,
        String gender,
        String size,
        Double weight,
        Integer age) {

    public Pet {
        FieldValidator.requiresNonNull("id", id);
        FieldValidator.requiresNonNull("name", name);
        FieldValidator.requiresNonNull("type", type);
        FieldValidator.requiresNonNull("size", size);
    }

}