package br.com.ipet.ordering.domain.model.schedule;

import br.com.ipet.ordering.domain.model.AggregateRoot;
import br.com.ipet.ordering.domain.model.FieldValidator;
import br.com.ipet.ordering.domain.model.company.CompanyId;
import br.com.ipet.ordering.domain.model.schedule.category.ServiceCategory;
import lombok.Builder;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.HashSet;
import java.util.Set;

import static br.com.ipet.ordering.domain.model.FieldValidator.requiresNonNull;
import static br.com.ipet.ordering.domain.model.schedule.category.ServiceCategory.HEALTH;
import static br.com.ipet.ordering.domain.model.schedule.category.ServiceCategory.HIGIENE;

public class DayTimeSchedule
        extends Schedule
        implements AggregateRoot<ScheduleId> {

    private Set<WorkDayTime> workingDays;

    private static final Set<ServiceCategory> SUPPORTED_CATEGORIES = Set.of(HIGIENE, HEALTH);

    static DayTimeSchedule create(CompanyId companyId, ScheduleName name, ServiceCategory serviceCategory) {
        if (!supports(serviceCategory))
            throw new ScheduleDontSupportSubcategoryException(serviceCategory);

        return new DayTimeSchedule(new ScheduleId(), companyId, name, serviceCategory,
                ScheduleStatus.DRAFT, new HashSet<>(), null,
                OffsetDateTime.now(ZoneOffset.UTC), new HashSet<>());
    }

    @Builder(builderClassName = "ExistingDayTimeScheduledBuilder", builderMethodName = "existing")
    public DayTimeSchedule(ScheduleId id, CompanyId companyId, ScheduleName name, ServiceCategory serviceCategory,
                           ScheduleStatus status, Set<LockedDate> lockedDates,
                           LocalDate startedAt, OffsetDateTime createdAt,
                           Set<WorkDayTime> workingDays) {
        super(id, companyId, name, serviceCategory, status, lockedDates, startedAt, createdAt);
        this.setWorkingDays(workingDays);
    }

    void addWorkingDayTime(DayOfWeek dayOfWeek, WorkingHours workingHours, CompanyId companyId) {
        requiresNonNull("dayOfWeek", dayOfWeek);
        requiresNonNull("workingHours", workingHours);
        requiresNonNull("companyId", companyId);

        this.verifyBelongToCompany(companyId);
        this.verifyExistingWorkingDay(dayOfWeek);

        var workingDay = WorkDayTime.createNew(this.id(), dayOfWeek, workingHours);
        this.workingDays.add(workingDay);
    }

    void removeWorkingDayTime(WorkingDayTimeId workingDayTimeId, CompanyId companyId) {
        requiresNonNull("companyId", companyId);

        this.verifyBelongToCompany(companyId);

        var workingDay = this.findWorkingDayTime(workingDayTimeId);
        this.workingDays.remove(workingDay);

        //Verificar qual status deve estar se remover todos workingDays
    }


    @Override
    protected void active(CompanyId companyId) {
        this.verifyBelongToCompany(companyId);

        if (workingDays.isEmpty()) {
            throw new DayTimeScheduleCannotBeActivedException("");
        }

        super.active(companyId);
    }

    void changeDayOfWeekWorkDay(WorkingDayTimeId workingDayTimeId, DayOfWeek dayOfWeek) {
        var workingDay = this.findWorkingDayTime(workingDayTimeId);
        this.verifyExistingWorkingDay(dayOfWeek);
        workingDay.changeDayOfWeek(dayOfWeek);
    }

    void changeWorkingHoursWorkDay(WorkingDayTimeId workingDayTimeId, WorkingHours workingHours) {
        var workingDay = this.findWorkingDayTime(workingDayTimeId);
        workingDay.changeWorkingHours(workingHours);
    }

    void changeLockedTimeWorkDay(WorkingDayTimeId workingDayTimeId, Set<LockedTime> lockedTimes) {
        var workingDay = this.findWorkingDayTime(workingDayTimeId);
        workingDay.changeLockedTime(lockedTimes);
    }

    private static boolean supports(ServiceCategory serviceCategory) {
        FieldValidator.requiresNonNull("serviceCategory", serviceCategory);
        return SUPPORTED_CATEGORIES.contains(serviceCategory);
    }

    private void verifyExistingWorkingDay(DayOfWeek dayOfWeek) {
        var existingWorkDay = this.workingDays.stream()
                .anyMatch(workDayTime -> workDayTime.dayOfWeek().equals(dayOfWeek));

        if (existingWorkDay)
            throw new WorkDayAlreadyExistsException();
    }

    private WorkDayTime findWorkingDayTime(WorkingDayTimeId workingDayTimeId) {
        requiresNonNull("workingDayId", workingDayTimeId);

        return this.workingDays.stream()
                .filter(workDayTime -> workDayTime.id().equals(workingDayTimeId))
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


    public Set<WorkDayTime> workingDays() {
        return workingDays;
    }

    private void setWorkingDays(Set<WorkDayTime> workingDays) {
        this.workingDays = workingDays;
    }

}
