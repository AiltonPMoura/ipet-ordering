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
    private Set<BlockedTimeSlot> blockedTimeSlotSlots;

    private static final int MINIMUM_AVAILABLE_WORK_MINUTES = 60;

    static AppointmentWorkDay createNew(ScheduleId scheduleId, DayOfWeek dayOfWeek, WorkingHours workingHours) {
        return new AppointmentWorkDay(new AppointmentWorkDayId(), scheduleId, dayOfWeek, workingHours, new HashSet<>());
    }

    @Builder(builderClassName = "ExistingWorkDayBuilder", builderMethodName = "existing")
    public AppointmentWorkDay(AppointmentWorkDayId id, ScheduleId scheduleId,
                              DayOfWeek dayOfWeek, WorkingHours workingHours, Set<BlockedTimeSlot> blockedTimeSlotSlots) {
        this.setId(id);
        this.setScheduleId(scheduleId);
        this.setDayOfWeek(dayOfWeek);
        this.setWorkingHours(workingHours);
        this.setBlockedTimeSlotSlots(blockedTimeSlotSlots);
    }

    void addBlockedTimeSlot(BlockedTimeSlot blockedTimeSlot) {
        FieldValidator.requiresNonNull("lockedTime", blockedTimeSlot);

        if (workingHours.notContains(blockedTimeSlot.startTime(), blockedTimeSlot.endTime()))
            throw new LockedTimeOutsideWorkingHoursException("");

        this.verifyConflictBlockedTime(blockedTimeSlot);
        this.verifyMinimumAvailableWorkingTime(blockedTimeSlot);

        this.blockedTimeSlotSlots.add(blockedTimeSlot);
    }

    void removeBlockedTimeSlot(BlockedTimeSlot blockedTimeSlot) {
        this.blockedTimeSlotSlots.remove(this.findBlockedTimeSlot(blockedTimeSlot));
    }

    void changeDayOfWeek(DayOfWeek dayOfWeek) {
        this.setDayOfWeek(dayOfWeek);
    }

    void changeWorkingHours(WorkingHours workingHours) {
        this.setWorkingHours(workingHours);
    }

    void changeBlockedTimeSlot(BlockedTimeSlot blockedTimeSlot, BlockedTimeSlot newBlockedTimeSlot) {
        this.removeBlockedTimeSlot(blockedTimeSlot);
        this.addBlockedTimeSlot(newBlockedTimeSlot);
    }

    private BlockedTimeSlot findBlockedTimeSlot(BlockedTimeSlot blockedTimeSlot) {
        FieldValidator.requiresNonNull("lockedTimes", blockedTimeSlotSlots);

        return blockedTimeSlotSlots.stream().filter(blockedTimeSlot::equals)
                .findFirst()
                .orElseThrow(() -> new LockedTimeNotFoundException(""));
    }

    private void verifyConflictBlockedTime(BlockedTimeSlot newBlockedTimeSlot) {
        var isConflictBlockedTime = this.blockedTimeSlotSlots.stream()
                .anyMatch(lockedTime -> lockedTime.verifyConflict(newBlockedTimeSlot));

        if (isConflictBlockedTime) throw new ConflictLockedTimeException("");
    }

    private void verifyMinimumAvailableWorkingTime(BlockedTimeSlot blockedTimeSlot) {
        var newMinutesLockedTime = this.totalLockedTime() + blockedTimeSlot.lockedMinutes();
        var workingMinutes = workingHours.workingMinutes();

        if (workingMinutes - newMinutesLockedTime < MINIMUM_AVAILABLE_WORK_MINUTES)
            throw new WorkingHoursCanNotBeLessThanOneHourException();
    }

    private long totalLockedTime() {
        return blockedTimeSlotSlots.stream().mapToLong(BlockedTimeSlot::lockedMinutes).sum();
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

    public Set<BlockedTimeSlot> lockedTimes() {
        return blockedTimeSlotSlots;
    }

    private void setBlockedTimeSlotSlots(Set<BlockedTimeSlot> blockedTimeSlotSlots) {
        this.blockedTimeSlotSlots = blockedTimeSlotSlots;
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
