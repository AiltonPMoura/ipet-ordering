package br.com.ipet.ordering.domain.model.agenda;

import br.com.ipet.ordering.domain.model.FieldValidator;
import br.com.ipet.ordering.domain.model.IdGenerator;

import java.util.UUID;

public record WorkingDayId(UUID value) {

    public WorkingDayId() {
        this(IdGenerator.generateTimeBasedEpochRandomGenerator());
    }

    public WorkingDayId {
        FieldValidator.requiresNonNull("WorkingDayId", value);
    }

}
