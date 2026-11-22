package br.com.ipet.ordering.domain.model.schedule.appointment;

import java.time.Duration;
import java.time.LocalTime;

import static br.com.ipet.ordering.domain.model.FieldValidator.requireStartTimeIsBeforeEndTime;
import static br.com.ipet.ordering.domain.model.FieldValidator.requireMinutePrecision;

public record LockedTime(LocalTime startTime, LocalTime endTime) {

    private static final int LOCKED_TIME_INTERVAL_MINUTES = 30;
    public static final int MINIMUM_LOCKED_TIME_MINUTES = 30;

    public LockedTime {
        requireMinutePrecision("start locked time", startTime);
        requireMinutePrecision("end locked time", endTime);
        requireStartTimeIsBeforeEndTime(startTime, endTime);

        this.verifyMinimumDuration(startTime, endTime);
        this.verifyValidInterval(startTime, endTime);
    }

    public boolean verifyConflict(LockedTime newLockedTime) {
        return startTime.isBefore(newLockedTime.endTime()) && endTime.isAfter(newLockedTime.startTime());
    }

    public long lockedMinutes() {
        return Duration.between(startTime, endTime).toMinutes();
    }

    private void verifyMinimumDuration(LocalTime startTime, LocalTime endTime) {
        var durationMinutes = Duration.between(startTime, endTime).toMinutes();

        if (durationMinutes < MINIMUM_LOCKED_TIME_MINUTES) {
            throw new MinimumLockedTimeIntervalException();
        }
    }

    private void verifyValidInterval(LocalTime startTime, LocalTime endTime) {
        var isInvalidIntervalStart = startTime.getMinute() % LOCKED_TIME_INTERVAL_MINUTES != 0;
        var isInvalidIntervalEnd = endTime.getMinute() % LOCKED_TIME_INTERVAL_MINUTES != 0;

        if (isInvalidIntervalStart || isInvalidIntervalEnd)
            throw new InvalidIntervalException();
    }

}
