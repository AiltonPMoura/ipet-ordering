package br.com.ipet.ordering.domain.model.schedule.appointment;

import br.com.ipet.ordering.domain.model.FieldValidator;
import br.com.ipet.ordering.domain.model.schedule.ScheduleId;
import lombok.Builder;

import java.time.DayOfWeek;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class AppointmentWorkDay {
    private AppointmentWorkDayId id;
    private ScheduleId scheduleId;
    private DayOfWeek dayOfWeek;
    private WorkingHours workingHours;
    private Set<LockedTime> lockedTimes;

    private static final int MINIMUM_AVAILABLE_WORK_MINUTES = 60;

    static AppointmentWorkDay createNew(ScheduleId scheduleId, DayOfWeek dayOfWeek, WorkingHours workingHours) {
        return new AppointmentWorkDay(new AppointmentWorkDayId(), scheduleId, dayOfWeek, workingHours, new HashSet<>());
    }

    @Builder(builderClassName = "ExistingWorkDayBuilder", builderMethodName = "existing")
    public AppointmentWorkDay(AppointmentWorkDayId id, ScheduleId scheduleId,
                              DayOfWeek dayOfWeek, WorkingHours workingHours, Set<LockedTime> lockedTimes) {
        this.setId(id);
        this.setScheduleId(scheduleId);
        this.setDayOfWeek(dayOfWeek);
        this.setWorkingHours(workingHours);
        this.setLockedTimes(lockedTimes);
    }

    void addLockedTime(LockedTime lockedTime) {
        FieldValidator.requiresNonNull("lockedTime", lockedTime);

        if (workingHours.notContains(lockedTime.startTime(), lockedTime.endTime()))
            throw new LockedTimeOutsideWorkingHoursException("");

        this.verifyConflictLockedTime(lockedTime);
        this.verifyMinimumAvailableWorkingTime(lockedTime);

        this.lockedTimes.add(lockedTime);
    }

    void removeLockedTime(LockedTime lockedTime) {
        this.lockedTimes.remove(this.findLockedTime(lockedTime));
    }

    void changeDayOfWeek(DayOfWeek dayOfWeek) {
        this.setDayOfWeek(dayOfWeek);
    }

    void changeWorkingHours(WorkingHours workingHours) {
        this.setWorkingHours(workingHours);
    }

    void changeLockedTime(LockedTime lockedTime, LockedTime newLockedTime) {
        this.removeLockedTime(lockedTime);
        this.addLockedTime(newLockedTime);
    }

    private LockedTime findLockedTime(LockedTime lockedTime) {
        FieldValidator.requiresNonNull("lockedTimes", lockedTimes);

        return lockedTimes.stream().filter(lockedTime::equals)
                .findFirst()
                .orElseThrow(() -> new LockedTimeNotFoundException(""));
    }

    private void verifyConflictLockedTime(LockedTime newLockedTime) {
        var isConflictLockedTime = this.lockedTimes.stream()
                .anyMatch(lockedTime -> lockedTime.verifyConflict(newLockedTime));

        if (isConflictLockedTime) throw new ConflictLockedTimeException("");
    }

    private void verifyMinimumAvailableWorkingTime(LockedTime lockedTime) {
        var newMinutesLockedTime = this.totalLockedTime() + lockedTime.lockedMinutes();
        var workingMinutes = workingHours.workingMinutes();

        if (workingMinutes - newMinutesLockedTime < MINIMUM_AVAILABLE_WORK_MINUTES)
            throw new WorkingHoursCanNotBeLessThanOneHourException();
    }

    private long totalLockedTime() {
        return lockedTimes.stream().mapToLong(LockedTime::lockedMinutes).sum();
    }

    public AppointmentWorkDayId id() {
        return id;
    }

    private void setId(AppointmentWorkDayId id) {
        FieldValidator.requiresNonNull("id", id);
        this.id = id;
    }

    public ScheduleId scheduleId() {
        return scheduleId;
    }

    private void setScheduleId(ScheduleId scheduleId) {
        FieldValidator.requiresNonNull("id", id);
        this.scheduleId = scheduleId;
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
        this.lockedTimes = lockedTimes;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof AppointmentWorkDay that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
