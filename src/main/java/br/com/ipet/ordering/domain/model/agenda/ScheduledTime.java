package br.com.ipet.ordering.domain.model.agenda;

import br.com.ipet.ordering.domain.model.FieldValidator;
import lombok.Builder;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ScheduledTime {
    private WorkingDayId id;
    private AgendaId agendaId;
    private DayOfWeek dayOfWeek;
    private WorkingHours workingHours;
    private Set<LockedTime> lockedTimes;

    @Builder(builderClassName = "CreateNewWorkingDay", builderMethodName = "createNew")
    private static ScheduledTime create(AgendaId agendaId, DayOfWeek dayOfWeek, WorkingHours workingHours) {
        return new ScheduledTime(new WorkingDayId(), agendaId, dayOfWeek, workingHours, new HashSet<>());
    }

    @Builder(builderClassName = "ExistingWorkingDayBuilder", builderMethodName = "existing")
    private ScheduledTime(WorkingDayId id, AgendaId agendaId, DayOfWeek dayOfWeek,
                          WorkingHours workingHours, Set<LockedTime> lockedTimes) {
        this.setId(id);
        this.setAgendaId(agendaId);
        this.setDayOfWeek(dayOfWeek);
        this.setWorkingHours(workingHours);
        this.setLockedTimes(lockedTimes);
    }

    void changeDayOfWeek(DayOfWeek dayOfWeek) {
        this.setDayOfWeek(dayOfWeek);
    }

    void changeWorkingHours(WorkingHours workingHours) {
        FieldValidator.requiresNonNull();
        this.setWorkingHours(workingHours);
    }

    void changeLockedTime(LockedTime lockedTime) {
        FieldValidator.requiresNonNull("lockedTime", lockedTime);
        this.setLockedTimes(lockedTimes);
    }

    void addLockedTime(LockedTime lockedTime) {
        verifyExistingLockedTime(lockedTime);
        this.lockedTimes.add(lockedTime);
    }

    void removeLockedTime(LockedTime lockedTime) {
        this.lockedTimes.remove(findLockedTime(lockedTime));
    }

    boolean isWorkingDay(LocalDate date) {
        return this.dayOfWeek.equals(date.getDayOfWeek());
    }

    private LockedTime findLockedTime(LockedTime lockedTime) {
        return lockedTimes.stream().filter(lockedTime::equals)
                .findFirst()
                .orElseThrow(() -> new LockedTimeNotFoundException());
    }

    Map<Integer, List<Integer>> availableTimes(MinuteInterval minuteInterval) {
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
        var timeOfWork = Duration.between(this.workingHours.startTime(), this.workingHours.endTime()).toMinutes();
        var currentTime = this.workingHours.startTime();

        for (var i = 0; i < timeOfWork; i += minuteInterval.value()) {
            var hour = currentTime.getHour();
            var minute = currentTime.getMinute();

            availableTimes.computeIfAbsent(hour, k -> new ArrayList<>()).add(minute);
            currentTime = currentTime.plusMinutes(minuteInterval.value());
        }
    }

    private void verifyExistingLockedTime(LockedTime lockedTimeNew) {
        var existingLockedTime = this.lockedTimes.stream().anyMatch(lockedTime ->
                (lockedTime.startTime().isBefore(lockedTimeNew.startTime()) && lockedTime.endTime().isAfter(lockedTimeNew.startTime()))
                        || (lockedTime.startTime().isBefore(lockedTimeNew.endTime()) && lockedTime.endTime().isAfter(lockedTimeNew.endTime()))
        );

        if (existingLockedTime)
            throw new RuntimeException();
    }

    public WorkingDayId id() {
        return id;
    }

    public AgendaId agendaId() {
        return agendaId;
    }

    public DayOfWeek dayOfWeek() {
        return dayOfWeek;
    }

    public WorkingHours workingHours() {
        return workingHours;
    }

    public Set<LockedTime> lockedTimes() {
        return lockedTimes;
    }

    private void setId(WorkingDayId id) {
        FieldValidator.requiresNonNull("WorkingDayId", id);
        this.id = id;
    }

    private void setAgendaId(AgendaId agendaId) {
        FieldValidator.requiresNonNull("agendaId", agendaId);
        this.agendaId = agendaId;
    }

    private void setDayOfWeek(DayOfWeek dayOfWeek) {
        FieldValidator.requiresNonNull("dayOfWeek", dayOfWeek);
        this.dayOfWeek = dayOfWeek;
    }

    private void setWorkingHours(WorkingHours workingHours) {
        FieldValidator.requiresNonNull("workingHours", workingHours);
        this.workingHours = workingHours;
    }

    private void setLockedTimes(Set<LockedTime> lockedTimes) {
        this.lockedTimes = lockedTimes;
    }

}
