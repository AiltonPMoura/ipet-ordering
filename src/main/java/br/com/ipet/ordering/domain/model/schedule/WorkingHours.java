package br.com.ipet.ordering.domain.model.schedule;

import br.com.ipet.ordering.domain.model.FieldValidator;

import java.time.Duration;
import java.time.OffsetTime;

public record WorkingHours(OffsetTime startTime, OffsetTime endTime) {

    private static final int MINIMUM_WORKING_HOURS = 1;
    private static final int MINIMUM_START_WORKING = 8;
    private static final int MAXIMUM_START_WORKING = 17;

    public WorkingHours {
        FieldValidator.requiresNonNull("starTime", startTime);
        FieldValidator.requiresNonNull("endTime", endTime);
        FieldValidator.requireEndTimeIsAfterStartTime(startTime, endTime);

        if (startTime.getHour() < MINIMUM_START_WORKING)
            throw new MinimumStartWorkingException();

        if (endTime.getHour() > MAXIMUM_START_WORKING)
            throw new MaximumEndWorkingException();

        var workingHours = Duration.between(startTime, endTime).toHours();

        if (workingHours < MINIMUM_WORKING_HOURS)
            throw new WorkingHoursCanNotBeLessThanOneException();
    }

}
