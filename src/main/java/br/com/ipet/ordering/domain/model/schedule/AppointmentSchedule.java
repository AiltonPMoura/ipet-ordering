package br.com.ipet.ordering.domain.model.schedule;

import br.com.ipet.ordering.domain.model.AggregateRoot;
import br.com.ipet.ordering.domain.model.company.CompanyId;
import lombok.Builder;

import java.time.DayOfWeek;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.HashSet;
import java.util.Set;

import static br.com.ipet.ordering.domain.model.FieldValidator.requiresNonNull;
import static br.com.ipet.ordering.domain.model.schedule.ServiceCategory.ACTIVITY;
import static br.com.ipet.ordering.domain.model.schedule.ServiceCategory.HEALTH;
import static br.com.ipet.ordering.domain.model.schedule.ServiceCategory.HYGIENE;

public class AppointmentSchedule
        extends Schedule
        implements AggregateRoot<ScheduleId> {

    private Set<AppointmentWorkDay> workDays;

    static AppointmentSchedule create(CompanyId companyId, ScheduleName name, ServiceCategory serviceCategory) {
        requiresNonNull("serviceCategory", serviceCategory);

        if (Set.of(HYGIENE, HEALTH, ACTIVITY).stream().noneMatch(serviceCategory::equals))
            throw new ScheduleDontSupportSubcategoryException(serviceCategory);

        return new AppointmentSchedule(new ScheduleId(), companyId, name, serviceCategory,
                ScheduleStatus.INACTIVE, new HashSet<>(),
                OffsetDateTime.now(ZoneOffset.UTC),
                new HashSet<>());
    }

    @Builder(builderClassName = "ExistingAppointmentScheduledBuilder", builderMethodName = "existing")
    public AppointmentSchedule(ScheduleId id, CompanyId companyId, ScheduleName name, ServiceCategory serviceCategory,
                               ScheduleStatus status, Set<LockedDate> lockedDates,
                               OffsetDateTime createdAt,
                               Set<AppointmentWorkDay> workDays) {
        super(id, companyId, name, serviceCategory, status, lockedDates, createdAt);
        this.setWorkDays(workDays);
    }

    void addWorkingDayTime(DayOfWeek dayOfWeek, WorkingHours workingHours, CompanyId companyId) {
        requiresNonNull("dayOfWeek", dayOfWeek);
        requiresNonNull("workingHours", workingHours);
        requiresNonNull("companyId", companyId);

        this.verifyBelongToCompany(companyId);
        this.verifyExistingWorkingDay(dayOfWeek);

        var workingDay = AppointmentWorkDay.createNew(this.id(), dayOfWeek, workingHours);
        this.workDays.add(workingDay);
    }

    void removeWorkingDayTime(AppointmentWorkDayId appointmentWorkDayId, CompanyId companyId) {
        requiresNonNull("companyId", companyId);

        this.verifyBelongToCompany(companyId);

        var workingDay = this.findWorkingDayTime(appointmentWorkDayId);
        this.workDays.remove(workingDay);

        //Verificar qual status deve estar se remover todos workingDays
    }


    @Override
    protected void active(CompanyId companyId) {
        this.verifyBelongToCompany(companyId);

        if (workDays.isEmpty()) {
            throw new DayTimeScheduleCannotBeActivedException("");
        }

        super.active(companyId);
    }

    void changeDayOfWeekWorkDay(AppointmentWorkDayId appointmentWorkDayId, DayOfWeek dayOfWeek) {
        var workingDay = this.findWorkingDayTime(appointmentWorkDayId);
        this.verifyExistingWorkingDay(dayOfWeek);
        workingDay.changeDayOfWeek(dayOfWeek);
    }

    void changeWorkingHoursWorkDay(AppointmentWorkDayId appointmentWorkDayId, WorkingHours workingHours) {
        var workingDay = this.findWorkingDayTime(appointmentWorkDayId);
        workingDay.changeWorkingHours(workingHours);
    }

    void changeLockedTimeWorkDay(AppointmentWorkDayId appointmentWorkDayId, Set<LockedTime> lockedTimes) {
        var workingDay = this.findWorkingDayTime(appointmentWorkDayId);
        workingDay.changeLockedTime(lockedTimes);
    }

    private void verifyExistingWorkingDay(DayOfWeek dayOfWeek) {
        var existingWorkDay = this.workDays.stream()
                .anyMatch(appointmentWorkDay -> appointmentWorkDay.dayOfWeek().equals(dayOfWeek));

        if (existingWorkDay)
            throw new WorkDayAlreadyExistsException();
    }

    private AppointmentWorkDay findWorkingDayTime(AppointmentWorkDayId appointmentWorkDayId) {
        requiresNonNull("workingDayTimeId", appointmentWorkDayId);

        return this.workDays.stream()
                .filter(appointmentWorkDay -> appointmentWorkDay.id().equals(appointmentWorkDayId))
                .findFirst()
                .orElseThrow(() -> new WorkinDayNotFoundException(""));
    }


    public Set<AppointmentWorkDay> workDays() {
        return workDays;
    }

    private void setWorkDays(Set<AppointmentWorkDay> workDays) {
        requiresNonNull("workDays", workDays);
        this.workDays = workDays;
    }

}
