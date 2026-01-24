package br.com.ipet.ordering.domain.model.commons;

import br.com.ipet.ordering.domain.model.util.FieldValidator;
import br.com.ipet.ordering.domain.model.util.IdGenerator;

import java.util.UUID;

public record WorkingDayId(UUID value) {

    public WorkingDayId() {
        this(IdGenerator.generateTimeBasedEpochRandomGenerator());
    }

    public WorkingDayId {
        FieldValidator.requiresNonNull("WorkingDayId", value);
    }

}
