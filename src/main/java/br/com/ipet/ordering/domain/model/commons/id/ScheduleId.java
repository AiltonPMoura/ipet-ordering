package br.com.ipet.ordering.domain.model.commons.id;

import br.com.ipet.ordering.domain.model.FieldValidator;
import br.com.ipet.ordering.domain.model.IdGenerator;

import java.util.UUID;

public record ScheduleId(UUID value) {

    public ScheduleId() {
        this(IdGenerator.generateTimeBasedEpochRandomGenerator());
    }

    public ScheduleId {
        FieldValidator.requiresNonNull("schedule id", value);
    }

}
