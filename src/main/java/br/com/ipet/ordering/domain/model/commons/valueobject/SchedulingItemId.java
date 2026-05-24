package br.com.ipet.ordering.domain.model.commons.valueobject;

import br.com.ipet.ordering.domain.model.FieldValidator;
import br.com.ipet.ordering.domain.model.IdGenerator;

import java.util.UUID;

public record SchedulingItemId(UUID value) {

    public SchedulingItemId() {
        this(IdGenerator.generateTimeBasedEpochRandomGenerator());
    }

    public SchedulingItemId {
        FieldValidator.requiresNonNull("scheduling item id", value);
    }

}
