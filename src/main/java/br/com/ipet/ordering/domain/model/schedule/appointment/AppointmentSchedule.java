package br.com.ipet.ordering.domain.model.schedule.appointment;

import br.com.ipet.ordering.domain.model.AggregateRoot;
import br.com.ipet.ordering.domain.model.DomainException;
import br.com.ipet.ordering.domain.model.company.CompanyId;
import br.com.ipet.ordering.domain.model.schedule.BlockedDate;
import br.com.ipet.ordering.domain.model.schedule.Schedule;
import br.com.ipet.ordering.domain.model.schedule.ScheduleDontSupportSubcategoryException;
import br.com.ipet.ordering.domain.model.schedule.ScheduleId;
import br.com.ipet.ordering.domain.model.schedule.ScheduleName;
import br.com.ipet.ordering.domain.model.schedule.ScheduleStatus;
import br.com.ipet.ordering.domain.model.schedule.ServiceCategory;
import lombok.Builder;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
                               ScheduleStatus status, Set<BlockedDate> blockedDates,
                               OffsetDateTime createdAt,
                               Set<AppointmentWorkDay> workDays) {
        super(id, companyId, name, serviceCategory, status, blockedDates, createdAt);
        this.setWorkDays(workDays);
    }

    public void addWorkDay(DayOfWeek dayOfWeek, WorkingHours workingHours, CompanyId companyId) {
        requiresNonNull("dayOfWeek", dayOfWeek);
        requiresNonNull("workingHours", workingHours);
        requiresNonNull("companyId", companyId);

        //this.verifyBelongToCompany(companyId);
        var existingWorkDay = this.workDays.stream()
                .anyMatch(appointmentWorkDay -> appointmentWorkDay.dayOfWeek().equals(dayOfWeek));

        if (existingWorkDay) throw new WorkDayAlreadyExistsException();

        var workingDay = AppointmentWorkDay.createNew(this.id(), dayOfWeek, workingHours);
        this.workDays.add(workingDay);
    }

    public void changeWorkDayWeek(AppointmentWorkDayId appointmentWorkDayId, DayOfWeek dayOfWeek, CompanyId companyId) {
        requiresNonNull("dayOfWeek", dayOfWeek);

        //this.verifyBelongToCompany(companyId);
        var workingDay = this.findWorkDay(appointmentWorkDayId);

        var existingWorkDay = this.workDays.stream()
                .anyMatch(appointmentWorkDay -> appointmentWorkDay.dayOfWeek().equals(dayOfWeek)
                        && !appointmentWorkDay.equals(workingDay));

        if (existingWorkDay) throw new WorkDayAlreadyExistsException();

        workingDay.changeDayOfWeek(dayOfWeek);
    }

    public void removeWorkDay(AppointmentWorkDayId appointmentWorkDayId, CompanyId companyId) {
        requiresNonNull("appointmentWorkDayId", appointmentWorkDayId);
        requiresNonNull("companyId", companyId);

        //this.verifyBelongToCompany(companyId);

        var workingDay = this.findWorkDay(appointmentWorkDayId);
        this.workDays.remove(workingDay);

        if (this.workDays.isEmpty()) this.deactivate(companyId);
    }

    @Override
    public void changeName(ScheduleName name, CompanyId companyId) {
        super.changeName(name, companyId);
    }

    @Override
    public void changeBlockedDates(Set<BlockedDate> blockedDates, CompanyId companyId) {
        super.changeBlockedDates(blockedDates, companyId);
    }

    public void changeWorkDayHours(AppointmentWorkDayId appointmentWorkDayId, WorkingHours workingHours, CompanyId companyId) {
        //this.verifyBelongToCompany(companyId);
        var workingDay = this.findWorkDay(appointmentWorkDayId);
        workingDay.changeWorkingHours(workingHours);
    }

    public void addWorkDayLockedTime(AppointmentWorkDayId appointmentWorkDayId, BlockedTimeSlot blockedTimeSlot, CompanyId companyId) {
        //this.verifyBelongToCompany(companyId);
        var workingDay = this.findWorkDay(appointmentWorkDayId);
        workingDay.addBlockedTimeSlot(blockedTimeSlot);
    }

    public void removeWorkDayLockedTime(AppointmentWorkDayId appointmentWorkDayId, BlockedTimeSlot blockedTimeSlot, CompanyId companyId) {
        //this.verifyBelongToCompany(companyId);
        var workingDay = this.findWorkDay(appointmentWorkDayId);
        workingDay.removeBlockedTimeSlot(blockedTimeSlot);
    }

    public void changeWorkDayLockedTime(AppointmentWorkDayId appointmentWorkDayId, BlockedTimeSlot blockedTimeSlot, BlockedTimeSlot newBlockedTimeSlot, CompanyId companyId) {
        //this.verifyBelongToCompany(companyId);
        var workingDay = this.findWorkDay(appointmentWorkDayId);
        workingDay.changeBlockedTimeSlot(blockedTimeSlot, newBlockedTimeSlot);
    }

    @Override
    public void activate(CompanyId companyId) {
        if (workDays.isEmpty())
            throw new AppointmentScheduleCannotBeActivedException("");

        super.activate(companyId);
    }

    @Override
    public void deactivate(CompanyId companyId) {
        super.deactivate(companyId);
    }

    public void verifyOperatingHours(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        verifyIfNotBlocked(startDateTime.toLocalDate());
        verifyIfWithinWorkingHours(startDateTime, endDateTime);
    }

    private void verifyIfNotBlocked(LocalDate startDate) {
        boolean isBlocked = this.blockedDates().stream()
                .map(BlockedDate::date)
                .anyMatch(startDate::equals);

        if (isBlocked) throw new DomainException("A data escolhida está bloqueada e não aceita agendamentos.");
    }

    private void verifyIfWithinWorkingHours(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        boolean isValid = this.workDays.stream()
                .anyMatch(workDay ->
                        workDay.dayOfWeek().equals(startDateTime.getDayOfWeek()) &&
                                !startDateTime.toLocalTime().isBefore(workDay.workingHours().startTime()) &&
                                !endDateTime.toLocalTime().isAfter(workDay.workingHours().endTime())
                );

        if (!isValid) {
            throw new DomainException("Fora do horário de expediente.");
        }
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
