package br.com.ipet.ordering.domain.model.schedule;

import br.com.ipet.ordering.domain.model.FieldValidator;
import br.com.ipet.ordering.domain.model.IdGenerator;

import java.util.UUID;

public record AppointmentWorkDayId(UUID value) {

    public AppointmentWorkDayId() {
        this(IdGenerator.generateTimeBasedEpochRandomGenerator());
    }

    public AppointmentWorkDayId {
        FieldValidator.requiresNonNull("AppointmentWorkDayId", value);
    }

}
