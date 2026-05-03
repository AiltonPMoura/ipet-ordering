package br.com.ipet.ordering.domain.model.schedule;

import br.com.ipet.ordering.domain.model.FieldValidator;

import java.time.Duration;
import java.time.OffsetTime;

public record LockedTime(OffsetTime startTime, OffsetTime endTime) {

    private static final int MINIMUM_LOCKED_TIME = 30;

    public LockedTime {
        FieldValidator.requiresNonNull("start locked time", startTime);
        FieldValidator.requiresNonNull("end locked time", endTime);
        FieldValidator.requireEndTimeIsAfterStartTime(startTime, endTime);

        var lockedTime = Duration.between(startTime, endTime).toMinutes();

        if (lockedTime < MINIMUM_LOCKED_TIME)
            throw new WorkingHoursCanNotBeLessThanOneException();

    }
}
