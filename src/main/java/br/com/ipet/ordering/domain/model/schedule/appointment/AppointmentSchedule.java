package br.com.ipet.ordering.domain.model.schedule.appointment;

import br.com.ipet.ordering.domain.model.AggregateRoot;
import br.com.ipet.ordering.domain.model.company.CompanyId;
import br.com.ipet.ordering.domain.model.schedule.LockedDate;
import br.com.ipet.ordering.domain.model.schedule.Schedule;
import br.com.ipet.ordering.domain.model.schedule.ScheduleDontSupportSubcategoryException;
import br.com.ipet.ordering.domain.model.schedule.ScheduleId;
import br.com.ipet.ordering.domain.model.schedule.ScheduleName;
import br.com.ipet.ordering.domain.model.schedule.ScheduleStatus;
import br.com.ipet.ordering.domain.model.schedule.ServiceCategory;
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

    public void addWorkDay(DayOfWeek dayOfWeek, WorkingHours workingHours, CompanyId companyId) {
        requiresNonNull("dayOfWeek", dayOfWeek);
        requiresNonNull("workingHours", workingHours);
        requiresNonNull("companyId", companyId);

        this.verifyBelongToCompany(companyId);
        this.verifyExistingWorkDay(dayOfWeek);

        var workingDay = AppointmentWorkDay.createNew(this.id(), dayOfWeek, workingHours);
        this.workDays.add(workingDay);
    }

    public void removeWorkDay(AppointmentWorkDayId appointmentWorkDayId, CompanyId companyId) {
        requiresNonNull("appointmentWorkDayId", appointmentWorkDayId);
        requiresNonNull("companyId", companyId);

        this.verifyBelongToCompany(companyId);

        var workingDay = this.findWorkDay(appointmentWorkDayId);
        this.workDays.remove(workingDay);

        if (this.workDays.isEmpty()) this.inactive(companyId);
    }

    @Override
    public void changeName(ScheduleName name, CompanyId companyId) {
        super.changeName(name, companyId);
    }

    @Override
    public void changeLockedDates(Set<LockedDate> lockedDates, CompanyId companyId) {
        super.changeLockedDates(lockedDates, companyId);
    }

    public void changeWorkDayWeek(AppointmentWorkDayId appointmentWorkDayId, DayOfWeek dayOfWeek, CompanyId companyId) {
        this.verifyBelongToCompany(companyId);
        this.verifyExistingWorkDay(dayOfWeek);
        var workingDay = this.findWorkDay(appointmentWorkDayId);
        workingDay.changeDayOfWeek(dayOfWeek);
    }

    public void changeWorkDayHours(AppointmentWorkDayId appointmentWorkDayId, WorkingHours workingHours, CompanyId companyId) {
        this.verifyBelongToCompany(companyId);
        var workingDay = this.findWorkDay(appointmentWorkDayId);
        workingDay.changeWorkingHours(workingHours);
    }

    public void addWorkDayLockedTime(AppointmentWorkDayId appointmentWorkDayId, LockedTime lockedTime, CompanyId companyId) {
        this.verifyBelongToCompany(companyId);
        var workingDay = this.findWorkDay(appointmentWorkDayId);
        workingDay.addLockedTime(lockedTime);
    }

    public void removeWorkDayLockedTime(AppointmentWorkDayId appointmentWorkDayId, LockedTime lockedTime, CompanyId companyId) {
        this.verifyBelongToCompany(companyId);
        var workingDay = this.findWorkDay(appointmentWorkDayId);
        workingDay.removeLockedTime(lockedTime);
    }

    public void changeWorkDayLockedTime(AppointmentWorkDayId appointmentWorkDayId, LockedTime lockedTime, LockedTime newLockedTime, CompanyId companyId) {
        this.verifyBelongToCompany(companyId);
        var workingDay = this.findWorkDay(appointmentWorkDayId);
        workingDay.changeLockedTime(lockedTime, newLockedTime);
    }

    @Override
    public void active(CompanyId companyId) {
        if (workDays.isEmpty())
            throw new AppointmentScheduleCannotBeActivedException("");

        super.active(companyId);
    }

    @Override
    public void inactive(CompanyId companyId) {
        super.inactive(companyId);
    }

    private void verifyExistingWorkDay(DayOfWeek dayOfWeek) {
        var existingWorkDay = this.workDays.stream()
                .anyMatch(appointmentWorkDay -> appointmentWorkDay.dayOfWeek().equals(dayOfWeek));

        if (existingWorkDay) throw new WorkDayAlreadyExistsException();
    }

    private AppointmentWorkDay findWorkDay(AppointmentWorkDayId appointmentWorkDayId) {
        requiresNonNull("appointmentWorkDayId", appointmentWorkDayId);

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
