package br.com.ipet.ordering.domain.model.schedule.appointment;

import br.com.ipet.ordering.domain.model.FieldValidator;
import br.com.ipet.ordering.domain.model.commons.id.ScheduleId;
import lombok.Builder;

import java.time.DayOfWeek;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static br.com.ipet.ordering.domain.model.schedule.appointment.WorkingHours.MINIMUM_WORKING_MINUTES;

public class AppointmentWorkDay {
    private AppointmentWorkDayId id;
    private ScheduleId scheduleId;
    private DayOfWeek dayOfWeek;
    private WorkingHours workingHours;
    private Set<LockedTimeSlot> lockedTimeSlots;

    static AppointmentWorkDay createNew(ScheduleId scheduleId, DayOfWeek dayOfWeek, WorkingHours workingHours) {
        return new AppointmentWorkDay(new AppointmentWorkDayId(), scheduleId, dayOfWeek, workingHours, new HashSet<>());
    }

    @Builder(builderClassName = "ExistingWorkDayBuilder", builderMethodName = "existing")
    public AppointmentWorkDay(AppointmentWorkDayId id, ScheduleId scheduleId,
                              DayOfWeek dayOfWeek, WorkingHours workingHours, Set<LockedTimeSlot> lockedTimeSlots) {
        this.setId(id);
        this.setScheduleId(scheduleId);
        this.setDayOfWeek(dayOfWeek);
        this.setWorkingHours(workingHours);
        this.setLockedTimeSlots(lockedTimeSlots);
    }

    void changeDayOfWeek(DayOfWeek dayOfWeek) {
        this.setDayOfWeek(dayOfWeek);
    }

    void changeWorkingHours(WorkingHours workingHours) {
        this.setWorkingHours(workingHours);
    }

    void changeLockedTimeSlots(Set<LockedTimeSlot> lockedTimeSlots) {
        FieldValidator.requiresNonNull("lockedTimes", lockedTimeSlots);

        var validSlots = this.findValidLockedTimes(lockedTimeSlots);
        this.verifyMinimumWorkingTime(validSlots);

        this.setLockedTimeSlots(validSlots);
    }

    private Set<LockedTimeSlot> findValidLockedTimes(Set<LockedTimeSlot> lockedTimeSlots) {
        var validSlots = new HashSet<LockedTimeSlot>();

        lockedTimeSlots.forEach(slot -> {
            FieldValidator.requiresNonNull("lockedTime", slot);

            if (workingHours.isNotWithinWorkingHours(slot.startTime(), slot.endTime()))
                throw new LockedTimeOutsideWorkingHoursException("");

            if (validSlots.stream().anyMatch(slot::verifyConflict))
                throw new ConflictLockedTimeException("");

            validSlots.add(slot);
        });

        return validSlots;
    }

    private void verifyMinimumWorkingTime(Set<LockedTimeSlot> lockedTimeSlots) {
        long totalLockedMinutes = lockedTimeSlots.stream()
                .mapToLong(LockedTimeSlot::lockedMinutes)
                .sum();

        if (workingHours.workingMinutes() - totalLockedMinutes < MINIMUM_WORKING_MINUTES)
            throw new MinimumWorkingHoursException();
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
        FieldValidator.requiresNonNull("scheduleId", scheduleId);
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
        return lockedTimeSlots;
    }

    private void setLockedTimeSlots(Set<LockedTimeSlot> lockedTimeSlots) {
        FieldValidator.requiresNonNull("lockedTimeSlots", lockedTimeSlots);
        this.lockedTimeSlots = lockedTimeSlots;
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
