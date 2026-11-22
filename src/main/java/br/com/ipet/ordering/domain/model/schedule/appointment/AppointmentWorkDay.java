package br.com.ipet.ordering.domain.model.schedule.appointment;

import br.com.ipet.ordering.domain.model.commons.id.ScheduleId;
import lombok.Builder;

import java.time.DayOfWeek;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static br.com.ipet.ordering.domain.model.FieldValidator.requiresNonNull;
import static br.com.ipet.ordering.domain.model.schedule.appointment.WorkingHours.MINIMUM_WORKING_MINUTES;

public class AppointmentWorkDay {
    private AppointmentWorkDayId id;
    private ScheduleId scheduleId;
    private DayOfWeek dayOfWeek;
    private WorkingHours workingHours;
    private Set<LockedTime> lockedTimes;

    static AppointmentWorkDay createNew(ScheduleId scheduleId, DayOfWeek dayOfWeek, WorkingHours workingHours) {
        return new AppointmentWorkDay(new AppointmentWorkDayId(), scheduleId, dayOfWeek, workingHours, new HashSet<>());
    }

    @Builder(builderClassName = "ExistingWorkDayBuilder", builderMethodName = "existing")
    private AppointmentWorkDay(AppointmentWorkDayId id, ScheduleId scheduleId,
                              DayOfWeek dayOfWeek, WorkingHours workingHours, Set<LockedTime> lockedTimes) {
        this.setId(id);
        this.setScheduleId(scheduleId);
        this.setDayOfWeek(dayOfWeek);
        this.setWorkingHours(workingHours);
        this.setLockedTimes(lockedTimes);
    }

    void changeDayOfWeek(DayOfWeek dayOfWeek) {
        this.setDayOfWeek(dayOfWeek);
    }

    void changeWorkingHours(WorkingHours workingHours) {
        requiresNonNull("workingHours", workingHours);

        this.verifyWorkingHoursAccommodateLockedTimes(workingHours);
        this.verifyMinimumWorkingTime(this.lockedTimes, workingHours);
        this.setWorkingHours(workingHours);
    }

    void changeLockedTimes(Set<LockedTime> lockedTimes) {
        requiresNonNull("lockedTimes", lockedTimes);

        this.verifyLockedTimes(lockedTimes);
        this.verifyMinimumWorkingTime(lockedTimes, this.workingHours);
        this.setLockedTimes(lockedTimes);
    }

    private void verifyWorkingHoursAccommodateLockedTimes(WorkingHours workingHours) {
        lockedTimes.forEach(lockedTime -> {
            if (workingHours.isNotStrictlyWithinWorkingHours(lockedTime.startTime(), lockedTime.endTime()))
                throw new LockedTimeOutsideWorkingHoursException("");
        });
    }

    private void verifyLockedTimes(Set<LockedTime> lockedTimes) {
        var verifiedLockedTimes = new HashSet<LockedTime>();

        lockedTimes.forEach(lockedTime -> {
            requiresNonNull("lockedTime", lockedTime);

            if (workingHours.isNotStrictlyWithinWorkingHours(lockedTime.startTime(), lockedTime.endTime()))
                throw new LockedTimeOutsideWorkingHoursException("");

            if (verifiedLockedTimes.stream().anyMatch(lockedTime::verifyConflict))
                throw new ConflictLockedTimeException("");

            verifiedLockedTimes.add(lockedTime);
        });

    }

    private void verifyMinimumWorkingTime(Set<LockedTime> lockedTimes, WorkingHours workingHours) {
        long totalLockedMinutes = lockedTimes.stream()
                .mapToLong(LockedTime::lockedMinutes)
                .sum();

        if (workingHours.workingMinutes() - totalLockedMinutes < MINIMUM_WORKING_MINUTES)
            throw new MinimumWorkingHoursException();
    }

    public AppointmentWorkDayId id() {
        return id;
    }

    private void setId(AppointmentWorkDayId id) {
        requiresNonNull("id", id);
        this.id = id;
    }

    public ScheduleId scheduleId() {
        return scheduleId;
    }

    private void setScheduleId(ScheduleId scheduleId) {
        requiresNonNull("scheduleId", scheduleId);
        this.scheduleId = scheduleId;
    }

    public DayOfWeek dayOfWeek() {
        return dayOfWeek;
    }

    private void setDayOfWeek(DayOfWeek dayOfWeek) {
        requiresNonNull("dayOfWeek", dayOfWeek);
        this.dayOfWeek = dayOfWeek;
    }

    public WorkingHours workingHours() {
        return workingHours;
    }

    private void setWorkingHours(WorkingHours workingHours) {
        requiresNonNull("workingHours", workingHours);
        this.workingHours = workingHours;
    }

    public Set<LockedTime> lockedTimes() {
        return Collections.unmodifiableSet(lockedTimes);
    }

    private void setLockedTimes(Set<LockedTime> lockedTimes) {
        requiresNonNull("lockedTimes", lockedTimes);
        this.lockedTimes = new HashSet<>(lockedTimes);
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
