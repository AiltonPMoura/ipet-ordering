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
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static br.com.ipet.ordering.domain.model.FieldValidator.requireNonNullMinutePrecision;
import static br.com.ipet.ordering.domain.model.FieldValidator.requiresNonNull;
import static br.com.ipet.ordering.domain.model.schedule.ServiceCategory.ACTIVITY;
import static br.com.ipet.ordering.domain.model.schedule.ServiceCategory.HEALTH;
import static br.com.ipet.ordering.domain.model.schedule.ServiceCategory.HYGIENE;

public class AppointmentSchedule
        extends Schedule
        implements AggregateRoot<ScheduleId> {

    public static final int REQUIRED_APPROVAL_MINUTES_FOR_NEXT_AVAILABLE_DAY = 60;

    private static final Set<ServiceCategory> SUPPORTED_CATEGORIES = Set.of(HYGIENE, HEALTH, ACTIVITY);

    private Set<AppointmentWorkDay> workDays;

    static AppointmentSchedule create(CompanyId companyId, ScheduleName name, ServiceCategory serviceCategory) {
        requiresNonNull("serviceCategory", serviceCategory);

        if (!SUPPORTED_CATEGORIES.contains(serviceCategory))
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

        if (this.hasWorkDay(dayOfWeek))
            throw new WorkDayAlreadyExistsException();

        var workingDay = AppointmentWorkDay.createNew(this.id(), dayOfWeek, workingHours);
        this.workDays.add(workingDay);
    }

    public void removeWorkDay(AppointmentWorkDayId appointmentWorkDayId, CompanyId companyId) {
        requiresNonNull("appointmentWorkDayId", appointmentWorkDayId);
        requiresNonNull("companyId", companyId);

        //this.verifyBelongToCompany(companyId);

        var workingDay = this.findWorkDay(appointmentWorkDayId);
        this.workDays.remove(workingDay);
    }

    public void changeWorkDayOfWeek(AppointmentWorkDayId appointmentWorkDayId, DayOfWeek dayOfWeek, CompanyId companyId) {
        requiresNonNull("appointmentWorkDayId", appointmentWorkDayId);
        requiresNonNull("dayOfWeek", dayOfWeek);
        requiresNonNull("companyId", companyId);

        //this.verifyBelongToCompany(companyId);
        var workingDay = this.findWorkDay(appointmentWorkDayId);

        if (this.hasAnotherWorkDay(dayOfWeek, workingDay))
            throw new WorkDayAlreadyExistsException();

        workingDay.changeDayOfWeek(dayOfWeek);
    }

    public void changeWorkDayHours(AppointmentWorkDayId appointmentWorkDayId, WorkingHours workingHours, CompanyId companyId) {
        requiresNonNull("appointmentWorkDayId", appointmentWorkDayId);
        requiresNonNull("workingHours", workingHours);
        //requiresNonNull("companyId", companyId);
        //this.verifyBelongToCompany(companyId);

        var workingDay = this.findWorkDay(appointmentWorkDayId);
        workingDay.changeWorkingHours(workingHours);
    }

    public void changeWorkDayLockedTime(AppointmentWorkDayId appointmentWorkDayId, Set<LockedTime> lockedTimes, CompanyId companyId) {
        requiresNonNull("appointmentWorkDayId", appointmentWorkDayId);
        requiresNonNull("lockedTimes", lockedTimes);
        //requiresNonNull("companyId", companyId);
        //this.verifyBelongToCompany(companyId);

        var workingDay = this.findWorkDay(appointmentWorkDayId);
        workingDay.changeLockedTimes(lockedTimes);
    }

    public void verifyAvailableDateTime(LocalDateTime scheduledStart, LocalDateTime scheduledEnd) {
        requiresNonNull("scheduledStart", scheduledStart);
        requiresNonNull("scheduledEnd", scheduledEnd);
        requireNonNullMinutePrecision("scheduledStart", scheduledStart.toLocalTime());
        requireNonNullMinutePrecision("scheduledEnd", scheduledEnd.toLocalTime());

        AppointmentSlotPolicy.verifyStartAligned(scheduledStart.toLocalTime());
        this.verifyLockedDate(scheduledStart.toLocalDate());
        var workDay = this.findWorkDay(scheduledStart.toLocalDate().getDayOfWeek());
        this.verifyWorkingTimes(workDay, scheduledStart.toLocalTime(), scheduledEnd.toLocalTime());
        this.verifyLockedTimes(workDay, new LockedTime(scheduledStart.toLocalTime(), scheduledEnd.toLocalTime()));
    }

    private boolean hasWorkDay(DayOfWeek dayOfWeek) {
        return this.workDays.stream().anyMatch(appointmentWorkDay -> appointmentWorkDay.dayOfWeek().equals(dayOfWeek));
    }

    private boolean hasAnotherWorkDay(DayOfWeek dayOfWeek, AppointmentWorkDay workDay) {
        return this.workDays.stream().anyMatch(appointmentWorkDay -> appointmentWorkDay.dayOfWeek().equals(dayOfWeek)
                && !appointmentWorkDay.equals(workDay));
    }

    private void verifyLockedDate(LocalDate scheduledDate) {
        var isLockedDate = this.lockedDates().stream().anyMatch(locked -> locked.date().equals(scheduledDate));

        if (isLockedDate)
            throw new DomainException("A data escolhida está bloqueada e não aceita agendamentos.");
    }

    private void verifyWorkingTimes(AppointmentWorkDay workDay, LocalTime scheduledStartTime, LocalTime scheduledEndTime) {
         if (workDay.workingHours().isNotWithinWorkingHours(scheduledStartTime, scheduledEndTime))
             throw new DomainException("Fora do horário de expediente.");
    }

    private void verifyLockedTimes(AppointmentWorkDay workDay, LockedTime lockedTimeSlot) {
        var isLockedTime = workDay.lockedTimes().stream().anyMatch(lockedTime -> lockedTime.verifyConflict(lockedTimeSlot));

        if (isLockedTime)
            throw new DomainException("O horário solicitado entra em conflito com uma pausa ou horário de almoço da loja.");
    }

    private AppointmentWorkDay findWorkDay(AppointmentWorkDayId appointmentWorkDayId) {
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
        return Collections.unmodifiableSet(workDays);
    }

    private void setWorkDays(Set<AppointmentWorkDay> workDays) {
        requiresNonNull("workDays", workDays);
        this.workDays = new HashSet<>(workDays);
    }

}
