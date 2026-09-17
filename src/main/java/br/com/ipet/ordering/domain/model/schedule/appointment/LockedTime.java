package br.com.ipet.ordering.domain.model.schedule.appointment;

import br.com.ipet.ordering.domain.model.FieldValidator;

import java.time.Duration;
import java.time.OffsetTime;

public record LockedTime(OffsetTime startTime, OffsetTime endTime) {

    private static final int LOCKED_TIME_INTERVAL_MINUTES = 60;

    public LockedTime {
        FieldValidator.requiresNonNull("start locked time", startTime);
        FieldValidator.requiresNonNull("end locked time", endTime);
        FieldValidator.requireStartTimeIsBeforeEndTime(startTime, endTime);

        this.verifyValidInterval(startTime, endTime);
    }

    public boolean verifyConflict(LockedTime newLockedTime) {
        return startTime.isBefore(newLockedTime.endTime()) && endTime.isAfter(newLockedTime.startTime());
    }

    public long lockedMinutes() {
        return Duration.between(startTime, endTime).toMinutes();
    }

    private void verifyValidInterval(OffsetTime startTime, OffsetTime endTime) {
        var isInvalidIntervalStart = startTime.getMinute() % LOCKED_TIME_INTERVAL_MINUTES != 0;
        var isInvalidIntervalEnd = endTime.getMinute() % LOCKED_TIME_INTERVAL_MINUTES != 0;

        if (isInvalidIntervalStart || isInvalidIntervalEnd)
            throw new InvalidIntervalException();
    }

}
