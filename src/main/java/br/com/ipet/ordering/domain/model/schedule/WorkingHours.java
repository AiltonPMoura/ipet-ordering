package br.com.ipet.ordering.domain.model.schedule;

import java.time.Duration;
import java.time.OffsetTime;
import java.time.ZoneOffset;

import static br.com.ipet.ordering.domain.model.FieldValidator.requireStartTimeIsBeforeEndTime;
import static br.com.ipet.ordering.domain.model.FieldValidator.requiresNonNull;

public record WorkingHours(OffsetTime startTime, OffsetTime endTime) {

    private static final OffsetTime MINIMUM_START_WORKING = OffsetTime.of(8, 0, 0, 0, ZoneOffset.UTC);
    private static final OffsetTime MAXIMUM_END_WORKING = OffsetTime.of(18, 0, 0, 0, ZoneOffset.UTC);
    private static final int WORKING_HOURS_INTERVAL_IN_MINUTES = 60;
    private static final int MINIMUM_WORKING_HOURS_IN_MINUTES = 60;

    public WorkingHours {
        requiresNonNull("startTime", startTime);
        requiresNonNull("endTime", endTime);
        requireStartTimeIsBeforeEndTime(startTime, endTime);
        this.verifyMinimumStartTime(startTime);
        this.verifyMaximumEndTime(endTime);
        this.verifyMinimumWorkingMinutes(startTime, endTime);
        this.verifyValidInterval(startTime, endTime);
    }

    public boolean contains(OffsetTime startTime, OffsetTime endTime) {
        requiresNonNull("startTime", startTime);
        requiresNonNull("endTime", endTime);

        return !startTime.isBefore(this.startTime) && !endTime.isAfter(this.endTime);
    }

    public boolean notContains(OffsetTime startTime, OffsetTime endTime) {
        return !contains(startTime, endTime);
    }

    public long workingMinutes() {
        return Duration.between(startTime, endTime).toMinutes();
    }

    private void verifyMinimumStartTime(OffsetTime startTime) {
        if (startTime.isBefore(MINIMUM_START_WORKING)) {
            throw new MinimumStartWorkingException();
        }
    }

    private void verifyMaximumEndTime(OffsetTime endTime) {
        if (endTime.isAfter(MAXIMUM_END_WORKING))
            throw new MaximumEndWorkingException();
    }

    private void verifyValidInterval(OffsetTime startTime, OffsetTime endTime) {
        var isInvalidIntervalStart = startTime.getMinute() % WORKING_HOURS_INTERVAL_IN_MINUTES != 0;
        var isInvalidIntervalEnd = endTime.getMinute() % WORKING_HOURS_INTERVAL_IN_MINUTES != 0;

        if (isInvalidIntervalStart || isInvalidIntervalEnd)
            throw new InvalidIntervalException();
    }

    private void verifyMinimumWorkingMinutes(OffsetTime startTime, OffsetTime endTime) {
        var workingMinutes = Duration.between(startTime, endTime).toMinutes();

        if (workingMinutes < MINIMUM_WORKING_HOURS_IN_MINUTES)
            throw new WorkingHoursCanNotBeLessThanOneHourException();
    }

}
