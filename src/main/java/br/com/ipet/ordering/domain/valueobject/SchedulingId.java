package br.com.ipet.ordering.domain.valueobject;

import br.com.ipet.ordering.domain.util.FieldValidator;
import br.com.ipet.ordering.domain.util.IdGenerator;

import java.util.UUID;

public record SchedulingId(UUID value) {

    public SchedulingId() {
        this(IdGenerator.generateTimeBasedEpochRandomGenerator());
    }

    public SchedulingId {
        FieldValidator.requiresNonNull("schedulingId", value);
    }

}
