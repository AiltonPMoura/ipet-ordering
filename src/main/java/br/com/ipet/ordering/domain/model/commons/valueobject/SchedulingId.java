package br.com.ipet.ordering.domain.model.commons.valueobject;

import br.com.ipet.ordering.domain.model.FieldValidator;
import br.com.ipet.ordering.domain.model.IdGenerator;

import java.util.UUID;

public record SchedulingId(UUID value) {

    public SchedulingId() {
        this(IdGenerator.generateTimeBasedEpochRandomGenerator());
    }

    public SchedulingId {
        FieldValidator.requiresNonNull("schedulingId", value);
    }

}
