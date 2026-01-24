package br.com.ipet.ordering.domain.model.commons;

import br.com.ipet.ordering.domain.model.util.FieldValidator;
import br.com.ipet.ordering.domain.model.util.IdGenerator;

import java.util.UUID;

public record SchedulingItemId(UUID value) {

    public SchedulingItemId() {
        this(IdGenerator.generateTimeBasedEpochRandomGenerator());
    }

    public SchedulingItemId {
        FieldValidator.requiresNonNull("schedulingItemId", value);
    }

}
