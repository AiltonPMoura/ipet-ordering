package br.com.ipet.ordering.domain.model.booking;

import br.com.ipet.ordering.domain.model.FieldValidator;
import br.com.ipet.ordering.domain.model.commons.id.PetId;
import br.com.ipet.ordering.domain.model.commons.enums.PetSize;
import br.com.ipet.ordering.domain.model.commons.enums.PetType;
import lombok.Builder;

@Builder
public record BookingPet(
        PetId id,
        PetType type,
        PetSize size,
        String name,
        String breed,
        String gender,
        Double weight,
        Integer age) {

    public BookingPet {
        FieldValidator.requiresNonNull("id", id);
        FieldValidator.requiresNonNull("name", name);
        FieldValidator.requiresNonNull("type", type);
        FieldValidator.requiresNonNull("size", size);
    }

}