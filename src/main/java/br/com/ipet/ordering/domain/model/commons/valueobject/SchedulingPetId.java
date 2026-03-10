package br.com.ipet.ordering.domain.model.commons.valueobject;

import br.com.ipet.ordering.domain.model.FieldValidator;
import br.com.ipet.ordering.domain.model.IdGenerator;

import java.util.UUID;

public record SchedulingPetId(UUID value) {

    public SchedulingPetId() {
        this(IdGenerator.generateTimeBasedEpochRandomGenerator());
    }

    public SchedulingPetId {
        FieldValidator.requiresNonNull("schedulingPetId", value);
    }

}
