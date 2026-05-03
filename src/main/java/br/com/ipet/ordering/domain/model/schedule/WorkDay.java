package br.com.ipet.ordering.domain.model.schedule;

import br.com.ipet.ordering.domain.model.FieldValidator;
import lombok.Builder;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class WorkDay {
    private WorkingDayId id;
    private DayOfWeek dayOfWeek;
    private WorkingHours workingHours;
    private Set<LockedTime> lockedTimes;

    static WorkDay createNew(DayOfWeek dayOfWeek, WorkingHours workingHours) {
        return new WorkDay(new WorkingDayId(), dayOfWeek, workingHours, new HashSet<>());
    }

    @Builder(builderMethodName = "ExistingWorkDayBuilder", buildMethodName = "existing")
    public WorkDay(WorkingDayId id, DayOfWeek dayOfWeek, WorkingHours workingHours, Set<LockedTime> lockedTimes) {
        this.setId(id);
        this.setDayOfWeek(dayOfWeek);
        this.setWorkingHours(workingHours);
        this.setLockedTimes(lockedTimes);
    }

    void changeDayOfWeek(DayOfWeek dayOfWeek) {
        this.setDayOfWeek(dayOfWeek);
    }

    void changeWorkingHours(WorkingHours workingHours) {
        this.setWorkingHours(workingHours);
    }

    void changeLockedTime(Set<LockedTime> lockedTimes) {
        this.setLockedTimes(lockedTimes);
    }

    void addLockedTime(LockedTime lockedTime) {
        this.verifyExistingLockedTime(lockedTime);
        this.lockedTimes.add(lockedTime);
    }

    void removeLockedTime(LockedTime lockedTime) {
        this.lockedTimes.remove(this.findLockedTime(lockedTime));
    }

    boolean isWorkingDay(LocalDate date) {
        return this.dayOfWeek.equals(date.getDayOfWeek());
    }

    private LockedTime findLockedTime(LockedTime lockedTime) {
        return lockedTimes.stream().filter(lockedTime::equals)
                .findFirst()
                .orElseThrow(() -> new LockedTimeNotFoundException(""));
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

    private void setId(WorkingDayId id) {
        FieldValidator.requiresNonNull("id", id);
        this.id = id;
    }

    public DayOfWeek dayOfWeek() {
        return dayOfWeek;
    }

    private void setDayOfWeek(DayOfWeek dayOfWeek) {
        FieldValidator.requiresNonNull("dayOfWeek", dayOfWeek);
        this.dayOfWeek = dayOfWeek;
    }

    public WorkingHours workingHours() {
        return workingHours;
    }

    private void setWorkingHours(WorkingHours workingHours) {
        FieldValidator.requiresNonNull("workingHours", workingHours);
        this.workingHours = workingHours;
    }

    public Set<LockedTime> lockedTimes() {
        return lockedTimes;
    }

    private void setLockedTimes(Set<LockedTime> lockedTimes) {
        FieldValidator.requiresNonNull("lockedTimes", lockedTimes);
        this.lockedTimes = lockedTimes;
    }
}
