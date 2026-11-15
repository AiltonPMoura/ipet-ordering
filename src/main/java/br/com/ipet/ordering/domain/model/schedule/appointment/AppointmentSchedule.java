package br.com.ipet.ordering.domain.model.schedule.appointment;

import br.com.ipet.ordering.domain.model.AggregateRoot;
import br.com.ipet.ordering.domain.model.DomainException;
import br.com.ipet.ordering.domain.model.commons.id.CompanyId;
import br.com.ipet.ordering.domain.model.schedule.LockedDate;
import br.com.ipet.ordering.domain.model.schedule.Schedule;
import br.com.ipet.ordering.domain.model.schedule.ScheduleDontSupportSubcategoryException;
import br.com.ipet.ordering.domain.model.commons.id.ScheduleId;
import br.com.ipet.ordering.domain.model.schedule.ScheduleName;
import br.com.ipet.ordering.domain.model.schedule.ScheduleStatus;
import br.com.ipet.ordering.domain.model.schedule.ServiceCategory;
import lombok.Builder;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

    public static final int SLOT_INCREMENT_MINUTES = 30;
    public static final int REQUIRED_APPROVAL_MINUTES_FOR_NEXT_AVAILABLE_DAY = 60;

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
    public void changeLockedDates(Set<LockedDate> lockedDates, CompanyId companyId) {
        super.changeLockedDates(lockedDates, companyId);
    }

    public void changeWorkDayHours(AppointmentWorkDayId appointmentWorkDayId, WorkingHours workingHours, CompanyId companyId) {
        //this.verifyBelongToCompany(companyId);
        var workingDay = this.findWorkDay(appointmentWorkDayId);
        workingDay.changeWorkingHours(workingHours);
    }

    public void changeWorkDayLockedTime(AppointmentWorkDayId appointmentWorkDayId, LockedTimeSlot lockedTimeSlot, LockedTimeSlot newLockedTimeSlot, CompanyId companyId) {
        //this.verifyBelongToCompany(companyId);
        var workingDay = this.findWorkDay(appointmentWorkDayId);
        workingDay.changeLockedTimeSlot(lockedTimeSlot, newLockedTimeSlot);
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

    public void verifyAvailableDateTime(LocalDateTime scheduledStart, LocalDateTime scheduledEnd) {
        requiresNonNull("scheduledStart", scheduledStart);
        requiresNonNull("scheduledEnd", scheduledEnd);

        this.verifyTimeSlotAlignment(scheduledStart);
        var workDay = this.requireWorkingDate(scheduledStart.toLocalDate());
        this.verifyWorkingTimes(workDay, scheduledStart.toLocalTime(), scheduledEnd.toLocalTime());
    }

    private void verifyTimeSlotAlignment(LocalDateTime scheduledStart) {
        int totalMinutes = (scheduledStart.getHour() * 60) + scheduledStart.getMinute();
        if (totalMinutes % SLOT_INCREMENT_MINUTES != 0) {
            throw new DomainException(String.format("O horário solicitado deve estar em intervalos de %d minutos.", SLOT_INCREMENT_MINUTES));
        }
    }

    private AppointmentWorkDay requireWorkingDate(LocalDate scheduledDate) {
        boolean isLockedDate = this.lockedDates().stream()
                .anyMatch(locked -> locked.date().equals(scheduledDate));

        if (isLockedDate) throw new DomainException("A data escolhida está bloqueada e não aceita agendamentos.");

        return this.findWorkDay(scheduledDate.getDayOfWeek());
    }

    private void verifyWorkingTimes(AppointmentWorkDay workDay, LocalTime scheduledStartTime, LocalTime scheduledEndTime) {
        var workingHours = workDay.workingHours();

        if (scheduledStartTime.isBefore(workingHours.startTime()) || scheduledEndTime.isAfter(workingHours.endTime())) {
            throw new DomainException("Fora do horário de expediente.");
        }

        boolean isLockedTime = workDay.lockedTimes().stream().anyMatch(lockedTime ->
                scheduledStartTime.isBefore(lockedTime.endTime()) && scheduledEndTime.isAfter(lockedTime.startTime())
        );

        if (isLockedTime) throw new DomainException("O horário solicitado entra em conflito com uma pausa ou horário de almoço da loja.");
    }

    private AppointmentWorkDay findWorkDay(AppointmentWorkDayId appointmentWorkDayId) {
        requiresNonNull("appointmentWorkDayId", appointmentWorkDayId);

        return this.workDays.stream()
                .filter(appointmentWorkDay -> appointmentWorkDay.id().equals(appointmentWorkDayId))
                .findFirst()
                .orElseThrow(() -> new WorkinDayNotFoundException(""));
    }

    private AppointmentWorkDay findWorkDay(DayOfWeek dayOfWeek) {
        requiresNonNull("dayOfWeek", dayOfWeek);

        return this.workDays.stream()
                .filter(appointmentWorkDay -> appointmentWorkDay.dayOfWeek().equals(dayOfWeek))
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
