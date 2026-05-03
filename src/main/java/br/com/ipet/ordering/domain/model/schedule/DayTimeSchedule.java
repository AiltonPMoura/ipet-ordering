package br.com.ipet.ordering.domain.model.schedule;

import br.com.ipet.ordering.domain.model.AggregateRoot;
import br.com.ipet.ordering.domain.model.FieldValidator;
import br.com.ipet.ordering.domain.model.company.CompanyId;
import lombok.Builder;

import java.time.DayOfWeek;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

public class DayTimeSchedule extends Schedule implements AggregateRoot<ScheduleId> {

    private Set<WorkDay> workingDays;

    static DayTimeSchedule create(CompanyId companyId, ScheduleName name) {
        return new DayTimeSchedule(new ScheduleId(), companyId, name,
                ScheduleStatus.DRAFT, OffsetDateTime.now(), new HashSet<>());
    }

    @Builder(builderClassName = "ExistingDayTimeScheduledBuilder", builderMethodName = "existing")
    public DayTimeSchedule(ScheduleId id, CompanyId companyId, ScheduleName name,
                           ScheduleStatus status, OffsetDateTime createdAt, Set<WorkDay> workingDays) {
        super(id, companyId, name, status, createdAt);
        this.setWorkingDays(workingDays);
    }

    void addWorkDay(DayOfWeek dayOfWeek, WorkingHours workingHours) {
        FieldValidator.requiresNonNull("dayOfWeek", dayOfWeek);
        this.verifyExistingWorkingDay(dayOfWeek);

        var workingDay = WorkDay.createNew(dayOfWeek, workingHours);
        this.workingDays.add(workingDay);
    }

    void removeWorkDay(WorkingDayId workingDayId) {
        var workingDay = this.findWorkingDay(workingDayId);
        this.workingDays.remove(workingDay);
    }

    void changeDayOfWeekWorkDay(WorkingDayId workingDayId, DayOfWeek dayOfWeek) {
        var workingDay = this.findWorkingDay(workingDayId);
        this.verifyExistingWorkingDay(dayOfWeek);
        workingDay.changeDayOfWeek(dayOfWeek);
    }

    void changeWorkingHoursWorkDay(WorkingDayId workingDayId, WorkingHours workingHours) {
        var workingDay = this.findWorkingDay(workingDayId);
        workingDay.changeWorkingHours(workingHours);
    }

    void changeLockedTimeWorkDay(WorkingDayId workingDayId, Set<LockedTime> lockedTimes) {
        var workingDay = this.findWorkingDay(workingDayId);
        workingDay.changeLockedTime(lockedTimes);
    }

    private void verifyExistingWorkingDay(DayOfWeek dayOfWeek) {
        var existingWorkDay = this.workingDays.stream()
                .anyMatch(workDay -> workDay.dayOfWeek().equals(dayOfWeek));

        if (existingWorkDay)
            throw new WorkDayAlreadyExistsException();
    }

    private WorkDay findWorkingDay(WorkingDayId workingDayId) {
        FieldValidator.requiresNonNull("workingDayId", workingDayId);

        return this.workingDays.stream()
                .filter(workDay -> workDay.id().equals(workingDayId))
                .findFirst()
                .orElseThrow(() -> new WorkinDayNotFoundException(""));
    }





    /*Map<Integer, List<Integer>> availableTimes(MinuteInterval minuteInterval) {
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
    }*/


    public Set<WorkDay> workingDays() {
        return workingDays;
    }

    public void setWorkingDays(Set<WorkDay> workingDays) {
        this.workingDays = workingDays;
    }

}
