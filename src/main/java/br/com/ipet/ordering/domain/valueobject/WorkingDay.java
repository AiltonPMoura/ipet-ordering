package br.com.ipet.ordering.domain.valueobject;

import br.com.ipet.ordering.domain.exception.WorkingHoursCanNotBeLessThanOneException;
import br.com.ipet.ordering.domain.util.FieldValidator;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.OffsetTime;
import java.util.Set;

public record WorkingDay(DayOfWeek dayOfWeek,
                         OffsetTime starTime,
                         OffsetTime endTime,
                         Set<LockedTime> lockedTimes) {

    private static final int MINIMUM_WORKING_HOURS = 1;

    public WorkingDay {
        FieldValidator.requiresNonNull("dayOfWeek", dayOfWeek);
        FieldValidator.requiresNonNull("starTime of dayOfWeek".concat(dayOfWeek.name()), starTime);
        FieldValidator.requiresNonNull("endTime eof dayOfWeek".concat(dayOfWeek.name()), endTime);

        var workingHours = Duration.between(starTime, endTime).toHours();

        if (workingHours < MINIMUM_WORKING_HOURS)
            throw new WorkingHoursCanNotBeLessThanOneException();
    }

}
