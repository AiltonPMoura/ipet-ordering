package br.com.ipet.ordering.domain.model.schedule;

import br.com.ipet.ordering.domain.model.FieldValidator;
import br.com.ipet.ordering.domain.model.IdGenerator;

import java.util.UUID;

public record WorkingDayTimeId(UUID value) {

    public WorkingDayTimeId() {
        this(IdGenerator.generateTimeBasedEpochRandomGenerator());
    }

    public WorkingDayTimeId {
        FieldValidator.requiresNonNull("WorkingDayTimeId", value);
    }

}
