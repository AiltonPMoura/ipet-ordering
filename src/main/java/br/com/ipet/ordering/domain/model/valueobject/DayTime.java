package br.com.ipet.ordering.domain.model.valueobject;

import br.com.ipet.ordering.domain.model.exception.WorkingHoursCanNotBeLessThanOneException;
import br.com.ipet.ordering.domain.model.util.FieldValidator;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.OffsetTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public record DayTime(DayOfWeek dayOfWeek, OffsetTime startTime, OffsetTime endTime) {

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

    public Map<Integer, List<Integer>> availableTimes() {

        var availableTimes = new HashMap<Integer, List<Integer>>();

        this.addTimes(availableTimes, startTime.getHour(), startTime.getMinute(), 59);

        IntStream.rangeClosed(startTime.plusHours(1).getHour(), endTime.plusHours(-1).getHour())
                .forEach(hour -> this.addTimes(availableTimes, hour, 0, 59));

        this.addTimes(availableTimes, endTime.getHour(), 0, endTime.getMinute());

        return availableTimes;
    }

    private void addTimes(HashMap<Integer, List<Integer>> availableTimes,
                          Integer hour, Integer startMinute, Integer endMinute) {

        availableTimes.put(hour, new ArrayList<>());
        var minutes = availableTimes.get(hour);
        IntStream.rangeClosed(startMinute, endMinute).forEach(minutes::add);
    }

}
