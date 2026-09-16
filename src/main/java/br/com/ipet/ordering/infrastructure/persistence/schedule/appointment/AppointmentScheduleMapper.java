package br.com.ipet.ordering.infrastructure.persistence.schedule.appointment;

import br.com.ipet.ordering.domain.model.company.CompanyId;
import br.com.ipet.ordering.domain.model.schedule.appointment.AppointmentSchedule;
import br.com.ipet.ordering.domain.model.schedule.appointment.AppointmentWorkDay;
import br.com.ipet.ordering.domain.model.schedule.appointment.AppointmentWorkDayId;
import br.com.ipet.ordering.domain.model.schedule.LockedDate;
import br.com.ipet.ordering.domain.model.schedule.appointment.LockedTime;
import br.com.ipet.ordering.domain.model.schedule.ScheduleId;
import br.com.ipet.ordering.domain.model.schedule.ScheduleName;
import br.com.ipet.ordering.domain.model.schedule.ScheduleStatus;
import br.com.ipet.ordering.domain.model.schedule.ServiceCategory;
import br.com.ipet.ordering.domain.model.schedule.appointment.WorkingHours;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class AppointmentScheduleMapper {

    public AppointmentSchedule toDomain(AppointmentSchedulePersistenceEntity appointmentSchedulePersistence) {
        return AppointmentSchedule.existing()
                .id(new ScheduleId(appointmentSchedulePersistence.getId()))
                .companyId(new CompanyId(appointmentSchedulePersistence.getCompanyId()))
                .name(new ScheduleName(appointmentSchedulePersistence.getName()))
                .serviceCategory(ServiceCategory.valueOf(appointmentSchedulePersistence.getServiceCategory()))
                .status(ScheduleStatus.valueOf(appointmentSchedulePersistence.getStatus()))
                .workDays(this.toWorkDays(appointmentSchedulePersistence.getWorkDays()))
                .lockedDates(this.toLockedDates(appointmentSchedulePersistence.getLockedDates()))
                .createdAt(appointmentSchedulePersistence.getCreatedAt())
                .build();
    }

    private Set<AppointmentWorkDay> toWorkDays(Set<AppointmentWorkDayPersistenceEntity> workDaysPersistence) {
        return workDaysPersistence.stream()
                .map(workDayPersistence -> AppointmentWorkDay.existing()
                        .id(new AppointmentWorkDayId(workDayPersistence.getId()))
                        .scheduleId(new ScheduleId(workDayPersistence.getAppointmentScheduleId()))
                        .dayOfWeek(workDayPersistence.getDayOfWeek())
                        .workingHours(new WorkingHours(workDayPersistence.getStartTime(), workDayPersistence.getEndTime()))
                        .lockedTimes(this.toLockedTimes(workDayPersistence.getLockedTimes()))
                        .build())
                .collect(Collectors.toSet());
    }

    private Set<LockedDate> toLockedDates(Set<LocalDate> lockedDates) {
        return lockedDates.stream().map(LockedDate::new).collect(Collectors.toSet());
    }

    private Set<LockedTime> toLockedTimes(Set<LockedTimeEmbeddable> lockedTimesEmbeddable) {
        return lockedTimesEmbeddable.stream()
                .map(lockedTimeEmbeddable ->
                        new LockedTime(lockedTimeEmbeddable.getStartTime(), lockedTimeEmbeddable.getEndTime()))
                .collect(Collectors.toSet());
    }

}
