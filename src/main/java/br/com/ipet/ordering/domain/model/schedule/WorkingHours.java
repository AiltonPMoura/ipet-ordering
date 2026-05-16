package br.com.ipet.ordering.domain.model.schedule;

import java.time.Duration;
import java.time.OffsetTime;
import java.time.ZoneOffset;

import static br.com.ipet.ordering.domain.model.FieldValidator.requireStartTimeIsBeforeEndTime;
import static br.com.ipet.ordering.domain.model.FieldValidator.requiresNonNull;

public record WorkingHours(OffsetTime startTime, OffsetTime endTime) {

    private static final OffsetTime MINIMUM_START_WORKING = OffsetTime.of(8, 0, 0, 0, ZoneOffset.UTC);
    private static final OffsetTime MAXIMUM_END_WORKING = OffsetTime.of(18, 0, 0, 0, ZoneOffset.UTC);
    private static final int MINIMUM_WORKING_HOURS_IN_MINUTES = 30;
    private static final int WORKING_HOURS_INTERVAL_IN_MINUTES = 30;

    public WorkingHours {
        requiresNonNull("startTime", startTime);
        requiresNonNull("endTime", endTime);
        requireStartTimeIsBeforeEndTime(startTime, endTime);
        this.verifyValidStartTime(startTime);
        this.verifyValidEndTime(endTime);
        this.verifyValidInterval(startTime, endTime);
        this.verifyMinimumWorkingMinutes(startTime, endTime);
    }

    void verifyMinimumWorkingMinutes(long minutesToSubtract) {
        var workingMinutes = this.calculateWorkingMinutes(startTime, endTime);

        if ((workingMinutes - minutesToSubtract) < MINIMUM_WORKING_HOURS_IN_MINUTES)
            throw new WorkingHoursCanNotBeLessThanHalfHourException();
    }

    private void verifyValidStartTime(OffsetTime startTime) {
        var maximumStartTime = MAXIMUM_END_WORKING.minusMinutes(30);

        if (startTime.isBefore(MINIMUM_START_WORKING) || startTime.isAfter(maximumStartTime)) {
            throw new MinimumStartWorkingException();
        }
    }

    private void verifyValidEndTime(OffsetTime endTime) {
        var minimumEndTime = MINIMUM_START_WORKING.plusMinutes(30);

        if (endTime.isAfter(MAXIMUM_END_WORKING) || endTime.isBefore(minimumEndTime))
            throw new MaximumEndWorkingException();
    }

    private void verifyValidInterval(OffsetTime startTime, OffsetTime endTime) {
        var isInvalidIntervalStart = startTime.getMinute() % WORKING_HOURS_INTERVAL_IN_MINUTES != 0;
        var isInvalidIntervalEnd = endTime.getMinute() % WORKING_HOURS_INTERVAL_IN_MINUTES != 0;

        if (isInvalidIntervalStart || isInvalidIntervalEnd)
            throw new InvalidIntervalException();
    }

    private void verifyMinimumWorkingMinutes(OffsetTime startTime, OffsetTime endTime) {
        var workingMinutes = this.calculateWorkingMinutes(startTime, endTime);

        if (workingMinutes < MINIMUM_WORKING_HOURS_IN_MINUTES)
            throw new WorkingHoursCanNotBeLessThanHalfHourException();
    }


    private long calculateWorkingMinutes(OffsetTime startTime, OffsetTime endTime) {
        return Duration.between(startTime, endTime).toMinutes();
    }

}
