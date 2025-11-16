package br.com.ipet.ordering.domain.valueobject;

import br.com.ipet.ordering.domain.util.FieldValidator;
import br.com.ipet.ordering.domain.util.IdGenerator;

import java.util.UUID;

public record SchedulingItemId(UUID value) {

    public SchedulingItemId() {
        this(IdGenerator.generateTimeBasedEpochRandomGenerator());
    }

    public SchedulingItemId {
        FieldValidator.requiresNonNull("schedulingItemId", value);
    }

}
