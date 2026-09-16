package br.com.ipet.ordering.infrastructure.persistence.schedule.appointment;

import br.com.ipet.ordering.domain.model.schedule.appointment.AppointmentSchedule;
import br.com.ipet.ordering.domain.model.schedule.appointment.AppointmentWorkDay;
import br.com.ipet.ordering.domain.model.schedule.LockedDate;
import br.com.ipet.ordering.domain.model.schedule.appointment.LockedTime;
import br.com.ipet.ordering.infrastructure.persistence.company.CompanyPersistenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AppointmentSchedulePersistenceMapper {

    private final CompanyPersistenceRepository companyPersistenceRepository;

    public AppointmentSchedulePersistenceEntity fromDomain(AppointmentSchedule appointmentSchedule) {
        return this.merge(new AppointmentSchedulePersistenceEntity(), appointmentSchedule);
    }

    public AppointmentSchedulePersistenceEntity merge(AppointmentSchedulePersistenceEntity appointmentSchedulePersistence,
                                                      AppointmentSchedule appointmentSchedule) {
        appointmentSchedulePersistence.setId(appointmentSchedule.id().value());
        appointmentSchedulePersistence.setCompany(companyPersistenceRepository.getReferenceById(appointmentSchedule.companyId().value()));
        appointmentSchedulePersistence.setServiceCategory(appointmentSchedule.serviceCategory().name());
        appointmentSchedulePersistence.setName(appointmentSchedule.name().value());
        appointmentSchedulePersistence.setStatus(appointmentSchedule.status().name());
        appointmentSchedulePersistence.setServiceCategory(appointmentSchedule.serviceCategory().name());
        appointmentSchedulePersistence.setWorkDays(this.mergeWorkDays(appointmentSchedulePersistence, appointmentSchedule));
        appointmentSchedulePersistence.setLockedDates(this.mergeLockedDates(appointmentSchedulePersistence, appointmentSchedule));
        return appointmentSchedulePersistence;
    }

    private Set<AppointmentWorkDayPersistenceEntity> mergeWorkDays(AppointmentSchedulePersistenceEntity appointmentSchedulePersistence,
                                                                   AppointmentSchedule appointmentSchedule) {
        var workDaysPersistence = appointmentSchedulePersistence.getWorkDays();
        var workDays = appointmentSchedule.workDays();

        if (workDaysPersistence.isEmpty())
            return workDays.stream().map(this::fromDomainWorkDay).collect(Collectors.toSet());

        var workDaysPersistenceMap = workDaysPersistence.stream()
                .collect(Collectors.toMap(AppointmentWorkDayPersistenceEntity::getId, item -> item));

        return workDays.stream().map(workDay -> {
            var workDayPersistence = workDaysPersistenceMap.getOrDefault(workDay.id().value(), new AppointmentWorkDayPersistenceEntity());
            return this.mergeWorkDay(workDayPersistence, workDay);
        }).collect(Collectors.toSet());
    }

    private AppointmentWorkDayPersistenceEntity fromDomainWorkDay(AppointmentWorkDay appointmentWorkDay) {
        return this.mergeWorkDay(new AppointmentWorkDayPersistenceEntity(), appointmentWorkDay);
    }

    private AppointmentWorkDayPersistenceEntity mergeWorkDay(AppointmentWorkDayPersistenceEntity workDayPersistence,
                                                             AppointmentWorkDay appointmentWorkDay) {
        workDayPersistence.setId(appointmentWorkDay.id().value());
        workDayPersistence.setDayOfWeek(appointmentWorkDay.dayOfWeek());
        workDayPersistence.setStartTime(appointmentWorkDay.workingHours().startTime());
        workDayPersistence.setEndTime(appointmentWorkDay.workingHours().endTime());
        workDayPersistence.setLockedTimes(this.mergeLockedTimes(workDayPersistence, appointmentWorkDay));
        return workDayPersistence;
    }

    private Set<LocalDate> mergeLockedDates(AppointmentSchedulePersistenceEntity appointmentSchedulePersistence,
                                            AppointmentSchedule appointmentSchedule) {
        var persistenceDates = appointmentSchedulePersistence.getLockedDates();

        persistenceDates.clear();

        persistenceDates.addAll(appointmentSchedule.lockedDates().stream()
                .map(LockedDate::date)
                .collect(Collectors.toSet()));

        return persistenceDates;
    }

    private Set<LockedTimeEmbeddable> mergeLockedTimes(AppointmentWorkDayPersistenceEntity workDayPersistence,
                                                       AppointmentWorkDay appointmentWorkDay) {

        var persistenceLockedTimes = workDayPersistence.getLockedTimes();

        persistenceLockedTimes.clear();

        persistenceLockedTimes.addAll(appointmentWorkDay.lockedTimes().stream()
                .map(this::toLockedTimeEmbeddable)
                .collect(Collectors.toSet()));

        return persistenceLockedTimes;
    }

    private LockedTimeEmbeddable toLockedTimeEmbeddable(LockedTime lockedTime) {
        return new LockedTimeEmbeddable(lockedTime.startTime(), lockedTime.endTime());
    }

}
