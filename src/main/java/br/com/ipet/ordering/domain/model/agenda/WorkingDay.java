package br.com.ipet.ordering.domain.model.agenda;

import br.com.ipet.ordering.domain.model.util.FieldValidator;
import br.com.ipet.ordering.domain.model.commons.AgendaId;
import br.com.ipet.ordering.domain.model.commons.DayTime;
import br.com.ipet.ordering.domain.model.commons.LockedTime;
import br.com.ipet.ordering.domain.model.commons.MinuteInterval;
import br.com.ipet.ordering.domain.model.commons.WorkingDayId;
import lombok.Builder;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class WorkingDay {
    private WorkingDayId id;
    private AgendaId agendaId;
    private DayTime dayTime;
    private Set<LockedTime> lockedTimes;

    @Builder(builderClassName = "CreateNewWorkingDay", builderMethodName = "createNew")
    private static WorkingDay create(AgendaId agendaId, DayTime dayTime) {
        return new WorkingDay(new WorkingDayId(), agendaId, dayTime, new HashSet<>());
    }

    @Builder(builderClassName = "ExistingWorkingDayBuilder", builderMethodName = "existing")
    private WorkingDay(WorkingDayId id, AgendaId agendaId,
                       DayTime dayTime, Set<LockedTime> lockedTimes) {
        this.setId(id);
        this.setAgendaId(agendaId);
        this.setDayTime(dayTime);
        this.setLockedTimes(lockedTimes);
    }

    protected void addLockedTime(LockedTime lockedTime) {
        verifyExistingLockedTime(lockedTime);
        this.lockedTimes.add(lockedTime);
    }

    protected void removeLockedTime(LockedTime lockedTime) {
        this.lockedTimes.remove(lockedTime);
    }

    protected boolean isWorkingDay(LocalDate date) {
        return this.dayTime.isSameDayOfWeek(date.getDayOfWeek());
    }

    protected Map<Integer, List<Integer>> availableTimes(MinuteInterval minuteInterval) {
        var availableTimes = new HashMap<Integer, List<Integer>>();

        addAvailableTimes(availableTimes,minuteInterval);
        removeLockedTimes(availableTimes);

        return availableTimes;
    }

    private void removeLockedTimes(HashMap<Integer, List<Integer>> availableTimes) {
        this.lockedTimes.forEach(lockedTime -> {
            var timeOfLocked = Duration.between(lockedTime.startTime(), lockedTime.endTime()).toMinutes();
            var currentLockedTime = lockedTime.startTime();

            for (var i = 0; i < timeOfLocked; i++) {
                var hour = currentLockedTime.getHour();
                var minute = currentLockedTime.getMinute();

                availableTimes.computeIfPresent(hour, (k, v) -> {
                    v.remove(Integer.valueOf(minute));
                    return v.isEmpty() ? null : v;
                });

                currentLockedTime = currentLockedTime.plusMinutes(1);
            }
        });
    }

    private void addAvailableTimes(HashMap<Integer, List<Integer>> availableTimes, MinuteInterval minuteInterval) {
        var timeOfWork = Duration.between(this.dayTime.startTime(), this.dayTime.endTime()).toMinutes();
        var currentTime = this.dayTime.startTime();

        for (var i = 0; i < timeOfWork; i += minuteInterval.value()) {
            var hour = currentTime.getHour();
            var minute = currentTime.getMinute();

            availableTimes.computeIfAbsent(hour, k -> new ArrayList<>()).add(minute);
            currentTime = currentTime.plusMinutes(minuteInterval.value());
        }
    }

    public WorkingDayId id() {
        return id;
    }

    public AgendaId agendaId() {
        return agendaId;
    }

    public DayTime dayTime() {
        return dayTime;
    }

    public Set<LockedTime> lockedTimes() {
        return lockedTimes;
    }

    private void verifyExistingLockedTime(LockedTime lockedTimeNew) {
        var existingLockedTime = this.lockedTimes.stream().anyMatch(lockedTime ->
                (lockedTime.startTime().isBefore(lockedTimeNew.startTime()) && lockedTime.endTime().isAfter(lockedTimeNew.startTime()))
                        || (lockedTime.startTime().isBefore(lockedTimeNew.endTime()) && lockedTime.endTime().isAfter(lockedTimeNew.endTime()))
        );

        if (existingLockedTime)
            throw new RuntimeException();
    }

    private void setId(WorkingDayId id) {
        FieldValidator.requiresNonNull("WorkingDayId", id);
        this.id = id;
    }

    private void setAgendaId(AgendaId agendaId) {
        FieldValidator.requiresNonNull("agendaId", agendaId);
        this.agendaId = agendaId;
    }

    private void setDayTime(DayTime dayTime) {
        FieldValidator.requiresNonNull("dayTime", dayTime);
        this.dayTime = dayTime;
    }

    private void setLockedTimes(Set<LockedTime> lockedTimes) {
        this.lockedTimes = lockedTimes;
    }

}
