package br.com.ipet.ordering.domain.model.schedule.appointment;

import java.time.Duration;
import java.time.LocalTime;

import static br.com.ipet.ordering.domain.model.FieldValidator.requireStartTimeIsBeforeEndTime;
import static br.com.ipet.ordering.domain.model.FieldValidator.requiresNonNull;

public record WorkingHours(LocalTime startTime, LocalTime endTime) {

    private static final LocalTime MINIMUM_START_WORKING = LocalTime.of(8, 0);
    private static final LocalTime MAXIMUM_END_WORKING = LocalTime.of(18, 0);
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

    public boolean contains(LocalTime startTime, LocalTime endTime) {
        requiresNonNull("startTime", startTime);
        requiresNonNull("endTime", endTime);

        return !startTime.isBefore(this.startTime) && !endTime.isAfter(this.endTime);
    }

    public boolean notContains(LocalTime startTime, LocalTime endTime) {
        return !contains(startTime, endTime);
    }

    public long workingMinutes() {
        return Duration.between(startTime, endTime).toMinutes();
    }

    private void verifyMinimumStartTime(LocalTime startTime) {
        if (startTime.isBefore(MINIMUM_START_WORKING)) {
            throw new MinimumStartWorkingException();
        }
    }

    private void verifyMaximumEndTime(LocalTime endTime) {
        if (endTime.isAfter(MAXIMUM_END_WORKING))
            throw new MaximumEndWorkingException();
    }

    private void verifyValidInterval(LocalTime startTime, LocalTime endTime) {
        var isInvalidIntervalStart = startTime.getMinute() % WORKING_HOURS_INTERVAL_IN_MINUTES != 0;
        var isInvalidIntervalEnd = endTime.getMinute() % WORKING_HOURS_INTERVAL_IN_MINUTES != 0;

        if (isInvalidIntervalStart || isInvalidIntervalEnd)
            throw new InvalidIntervalException();
    }

    private void verifyMinimumWorkingMinutes(LocalTime startTime, LocalTime endTime) {
        var workingMinutes = Duration.between(startTime, endTime).toMinutes();

        if (workingMinutes < MINIMUM_WORKING_HOURS_IN_MINUTES)
            throw new WorkingHoursCanNotBeLessThanOneHourException();
    }

}
