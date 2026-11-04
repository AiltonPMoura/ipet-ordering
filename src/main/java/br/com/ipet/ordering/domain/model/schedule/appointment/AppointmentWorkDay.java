package br.com.ipet.ordering.domain.model.schedule.appointment;

import br.com.ipet.ordering.domain.model.FieldValidator;
import br.com.ipet.ordering.domain.model.commons.id.ScheduleId;
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
    private Set<LockedTimeSlot> lockedTimeSlotSlots;

    private static final int MINIMUM_AVAILABLE_WORK_MINUTES = 60;

    static AppointmentWorkDay createNew(ScheduleId scheduleId, DayOfWeek dayOfWeek, WorkingHours workingHours) {
        return new AppointmentWorkDay(new AppointmentWorkDayId(), scheduleId, dayOfWeek, workingHours, new HashSet<>());
    }

    @Builder(builderClassName = "ExistingWorkDayBuilder", builderMethodName = "existing")
    public AppointmentWorkDay(AppointmentWorkDayId id, ScheduleId scheduleId,
                              DayOfWeek dayOfWeek, WorkingHours workingHours, Set<LockedTimeSlot> lockedTimeSlotSlots) {
        this.setId(id);
        this.setScheduleId(scheduleId);
        this.setDayOfWeek(dayOfWeek);
        this.setWorkingHours(workingHours);
        this.setLockedTimeSlotSlots(lockedTimeSlotSlots);
    }

    void addLockedTimeSlot(LockedTimeSlot lockedTimeSlot) {
        FieldValidator.requiresNonNull("lockedTime", lockedTimeSlot);

        if (workingHours.notContains(lockedTimeSlot.startTime(), lockedTimeSlot.endTime()))
            throw new LockedTimeOutsideWorkingHoursException("");

        this.verifyConflictLockedTime(lockedTimeSlot);
        this.verifyMinimumAvailableWorkingTime(lockedTimeSlot);

        this.lockedTimeSlotSlots.add(lockedTimeSlot);
    }

    void removeLockedTimeSlot(LockedTimeSlot lockedTimeSlot) {
        this.lockedTimeSlotSlots.remove(this.findLockedTimeSlot(lockedTimeSlot));
    }

    void changeDayOfWeek(DayOfWeek dayOfWeek) {
        this.setDayOfWeek(dayOfWeek);
    }

    void changeWorkingHours(WorkingHours workingHours) {
        this.setWorkingHours(workingHours);
    }

    void changeLockedTimeSlot(LockedTimeSlot lockedTimeSlot, LockedTimeSlot newLockedTimeSlot) {
        this.removeLockedTimeSlot(lockedTimeSlot);
        this.addLockedTimeSlot(newLockedTimeSlot);
    }

    private LockedTimeSlot findLockedTimeSlot(LockedTimeSlot lockedTimeSlot) {
        FieldValidator.requiresNonNull("lockedTimes", lockedTimeSlotSlots);

        return lockedTimeSlotSlots.stream().filter(lockedTimeSlot::equals)
                .findFirst()
                .orElseThrow(() -> new LockedTimeNotFoundException(""));
    }

    private void verifyConflictLockedTime(LockedTimeSlot newLockedTimeSlot) {
        var isConflictLockedTime = this.lockedTimeSlotSlots.stream()
                .anyMatch(lockedTime -> lockedTime.verifyConflict(newLockedTimeSlot));

        if (isConflictLockedTime) throw new ConflictLockedTimeException("");
    }

    private void verifyMinimumAvailableWorkingTime(LockedTimeSlot lockedTimeSlot) {
        var newMinutesLockedTime = this.totalLockedTime() + lockedTimeSlot.lockedMinutes();
        var workingMinutes = workingHours.workingMinutes();

        if (workingMinutes - newMinutesLockedTime < MINIMUM_AVAILABLE_WORK_MINUTES)
            throw new WorkingHoursCanNotBeLessThanOneHourException();
    }

    private long totalLockedTime() {
        return lockedTimeSlotSlots.stream().mapToLong(LockedTimeSlot::lockedMinutes).sum();
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

    public Set<LockedTimeSlot> lockedTimes() {
        return lockedTimeSlotSlots;
    }

    private void setLockedTimeSlotSlots(Set<LockedTimeSlot> lockedTimeSlotSlots) {
        this.lockedTimeSlotSlots = lockedTimeSlotSlots;
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
