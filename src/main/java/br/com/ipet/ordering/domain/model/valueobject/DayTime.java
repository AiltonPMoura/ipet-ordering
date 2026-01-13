package br.com.ipet.ordering.domain.model.valueobject;

import br.com.ipet.ordering.domain.model.exception.WorkingHoursCanNotBeLessThanOneException;
import br.com.ipet.ordering.domain.model.util.FieldValidator;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.OffsetTime;

public record DayTime(DayOfWeek dayOfWeek,
                      OffsetTime startTime,
                      OffsetTime endTime,
                      Integer minuteInterval) {

    private static final int MINIMUM_WORKING_HOURS = 1;
    private static final int MINIMUM_MINUTE_INTERVAL = 10;
    private static final int MAXIMUM_MINUTE_INTERVAL = 30;

    public DayTime(DayOfWeek dayOfWeek,
                   OffsetTime startTime,
                   OffsetTime endTime) {
        this(dayOfWeek, startTime, endTime, MINIMUM_MINUTE_INTERVAL);
    }

    public DayTime {
        FieldValidator.requiresNonNull("dayOfWeek", dayOfWeek);
        FieldValidator.requiresNonNull("starTime of dayOfWeek".concat(dayOfWeek.name()), startTime);
        FieldValidator.requiresNonNull("starTime of dayOfWeek".concat(dayOfWeek.name()), startTime);
        FieldValidator.requiresNonNull("minute interval".concat(dayOfWeek.name()), minuteInterval);
        FieldValidator.requireEndTimeIsAfterStartTime(startTime, endTime);

        var workingHours = Duration.between(startTime, endTime).toHours();

        if (workingHours < MINIMUM_WORKING_HOURS)
            throw new WorkingHoursCanNotBeLessThanOneException();

        if (workingHours < MINIMUM_MINUTE_INTERVAL)
            throw new RuntimeException();

        if (workingHours > MAXIMUM_MINUTE_INTERVAL)
            throw new RuntimeException();
    }

    public boolean isSameDayOfWeek(DayOfWeek dayOfWeek) {
        return this.dayOfWeek.equals(dayOfWeek);
    }
}
