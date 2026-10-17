package br.com.ipet.ordering.domain.model.booking;

import br.com.ipet.ordering.domain.model.FieldValidator;
import br.com.ipet.ordering.domain.model.pet.PetId;
import br.com.ipet.ordering.domain.model.pet.PetSize;
import br.com.ipet.ordering.domain.model.pet.PetType;
import lombok.Builder;

@Builder
public record Pet(
        PetId id,
        PetType type,
        PetSize size,
        String name,
        String breed,
        String gender,
        Double weight,
        Integer age) {

    public Pet {
        FieldValidator.requiresNonNull("id", id);
        FieldValidator.requiresNonNull("name", name);
        FieldValidator.requiresNonNull("type", type);
        FieldValidator.requiresNonNull("size", size);
    }

}