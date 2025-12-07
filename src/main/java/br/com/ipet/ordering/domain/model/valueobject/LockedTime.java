package br.com.ipet.ordering.domain.model.valueobject;

import br.com.ipet.ordering.domain.model.util.FieldValidator;

import java.time.OffsetTime;

public record LockedTime(OffsetTime startTime, OffsetTime endTime) {

    public LockedTime {
        FieldValidator.requiresNonNull("start locked time", startTime);
        FieldValidator.requiresNonNull("end locked time", endTime);
        FieldValidator.requireEndTimeIsAfterStartTime("locked time", startTime, endTime);

    }
}
