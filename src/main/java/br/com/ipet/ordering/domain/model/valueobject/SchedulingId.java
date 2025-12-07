package br.com.ipet.ordering.domain.model.valueobject;

import br.com.ipet.ordering.domain.model.util.FieldValidator;
import br.com.ipet.ordering.domain.model.util.IdGenerator;

import java.util.UUID;

public record SchedulingId(UUID value) {

    public SchedulingId() {
        this(IdGenerator.generateTimeBasedEpochRandomGenerator());
    }

    public SchedulingId {
        FieldValidator.requiresNonNull("schedulingId", value);
    }

}
