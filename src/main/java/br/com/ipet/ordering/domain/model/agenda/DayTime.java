package br.com.ipet.ordering.domain.model.agenda;

import br.com.ipet.ordering.domain.model.FieldValidator;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.OffsetTime;

public record DayTime(DayOfWeek dayOfWeek,
                      OffsetTime startTime,
                      OffsetTime endTime) {

    private static final int MINIMUM_WORKING_HOURS = 1;

    public DayTime {
        FieldValidator.requiresNonNull("dayOfWeek", dayOfWeek);
        FieldValidator.requiresNonNull("starTime of dayOfWeek".concat(dayOfWeek.name()), startTime);
        FieldValidator.requiresNonNull("starTime of dayOfWeek".concat(dayOfWeek.name()), startTime);
        FieldValidator.requireEndTimeIsAfterStartTime(startTime, endTime);

        var workingHours = Duration.between(startTime, endTime).toHours();

        if (workingHours < MINIMUM_WORKING_HOURS)
            throw new WorkingHoursCanNotBeLessThanOneException();
    }

    public boolean isSameDayOfWeek(DayOfWeek dayOfWeek) {
        return this.dayOfWeek.equals(dayOfWeek);
    }
}
