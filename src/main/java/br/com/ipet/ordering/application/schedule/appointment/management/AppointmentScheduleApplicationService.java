package br.com.ipet.ordering.application.schedule.appointment.management;

import br.com.ipet.ordering.application.schedule.ScheduleInput;
import br.com.ipet.ordering.domain.model.company.CompanyId;
import br.com.ipet.ordering.domain.model.schedule.LockedDate;
import br.com.ipet.ordering.domain.model.schedule.ScheduleId;
import br.com.ipet.ordering.domain.model.schedule.ScheduleName;
import br.com.ipet.ordering.domain.model.schedule.ScheduleNotFoundException;
import br.com.ipet.ordering.domain.model.schedule.ServiceCategory;
import br.com.ipet.ordering.domain.model.schedule.appointment.AppointmentSchedule;
import br.com.ipet.ordering.domain.model.schedule.appointment.AppointmentScheduleRegistrationService;
import br.com.ipet.ordering.domain.model.schedule.appointment.AppointmentSchedules;
import br.com.ipet.ordering.domain.model.schedule.appointment.AppointmentWorkDayId;
import br.com.ipet.ordering.domain.model.schedule.appointment.LockedTime;
import br.com.ipet.ordering.domain.model.schedule.appointment.WorkingHours;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static br.com.ipet.ordering.domain.model.FieldValidator.requiresNonNull;


@Service
@RequiredArgsConstructor
@Transactional
public class AppointmentScheduleApplicationService {

    private final AppointmentScheduleRegistrationService appointmentScheduleRegistrationService;
    private final AppointmentSchedules appointmentSchedules;

    public UUID create(ScheduleInput input) {
        requiresNonNull("input", input);

        var appointmentSchedule = appointmentScheduleRegistrationService.register(
                new CompanyId(input.companyId()),
                new ScheduleName(input.name()),
                ServiceCategory.valueOf(input.subCategory())
        );

        appointmentSchedules.add(appointmentSchedule);

        return appointmentSchedule.id().value();
    }

    public void changeName(UUID scheduleId, UUID companyId, String name) {
        requiresNonNull("name", name);

        var appointmentSchedule = this.findAppointmentSchedule(scheduleId);
        appointmentSchedule.changeName(new ScheduleName(name), new CompanyId(companyId));

        appointmentSchedules.add(appointmentSchedule);
    }

    public void changeLockedDates(UUID scheduleId, UUID companyId, List<LocalDate> dates) {
        requiresNonNull("dates", dates);

        var appointmentSchedule = this.findAppointmentSchedule(scheduleId);
        var lockedDates = dates.stream().map(LockedDate::new).collect(Collectors.toSet());
        appointmentSchedule.changeLockedDates(lockedDates, new CompanyId(companyId));

        appointmentSchedules.add(appointmentSchedule);
    }

    public void addWorkDay(UUID scheduleId, UUID companyId, WorkDayInput input) {
        requiresNonNull("input", input);

        var appointmentSchedule = this.findAppointmentSchedule(scheduleId);
        appointmentSchedule.addWorkDay(input.dayOfWeek(), new WorkingHours(input.startTime(), input.endTime()), new CompanyId(companyId));

        appointmentSchedules.add(appointmentSchedule);
    }

    public void removeWorkDay(UUID scheduleId, UUID companyId, UUID workDayId) {
        requiresNonNull("workDayId", workDayId);

        var appointmentSchedule = this.findAppointmentSchedule(scheduleId);
        appointmentSchedule.removeWorkDay(new AppointmentWorkDayId(workDayId), new CompanyId(companyId));

        appointmentSchedules.add(appointmentSchedule);
    }

    public void updateWorkDay(UUID scheduleId, UUID companyId, UUID workDayId, WorkDayInput input) {
        requiresNonNull("workDayId", workDayId);
        requiresNonNull("input", input);

        var appointmentSchedule = this.findAppointmentSchedule(scheduleId);
        appointmentSchedule.changeWorkDayWeek(new AppointmentWorkDayId(workDayId), input.dayOfWeek(), new CompanyId(companyId));
        appointmentSchedule.changeWorkDayHours(new AppointmentWorkDayId(workDayId), new WorkingHours(input.startTime(), input.endTime()), new CompanyId(companyId));

        appointmentSchedules.add(appointmentSchedule);
    }

    public void addLockedTime(UUID scheduleId, UUID workDayId, UUID companyId, LockedTimeInput input) {
        requiresNonNull("input", input);

        var appointmentSchedule = this.findAppointmentSchedule(scheduleId);

        appointmentSchedule.addWorkDayLockedTime(
                new AppointmentWorkDayId(workDayId),
                new LockedTime(input.startTime(), input.endTime()),
                new CompanyId(companyId)
        );

        appointmentSchedules.add(appointmentSchedule);
    }

    public void removeLockedTime(UUID scheduleId, UUID workDayId, UUID companyId, LockedTimeInput input) {
        requiresNonNull("input", input);

        var appointmentSchedule = this.findAppointmentSchedule(scheduleId);

        appointmentSchedule.removeWorkDayLockedTime(
                new AppointmentWorkDayId(workDayId),
                new LockedTime(input.startTime(), input.endTime()),
                new CompanyId(companyId)
        );

        appointmentSchedules.add(appointmentSchedule);
    }

    public void changeLockedTime(UUID scheduleId, UUID workDayId, UUID companyId, LockedTimeInput oldInput, LockedTimeInput newInput) {
        requiresNonNull("oldInput", oldInput);
        requiresNonNull("newInput", newInput);

        var appointmentSchedule = this.findAppointmentSchedule(scheduleId);

        appointmentSchedule.changeWorkDayLockedTime(
                new AppointmentWorkDayId(workDayId),
                new LockedTime(oldInput.startTime(), oldInput.endTime()),
                new LockedTime(newInput.startTime(), newInput.endTime()),
                new CompanyId(companyId)
        );

        appointmentSchedules.add(appointmentSchedule);
    }

    public void activate(UUID scheduleId, UUID companyId) {
        var appointmentSchedule = this.findAppointmentSchedule(scheduleId);
        appointmentSchedule.activate(new CompanyId(companyId));
        appointmentSchedules.add(appointmentSchedule);
    }

    public void deactivate(UUID scheduleId, UUID companyId) {
        var appointmentSchedule = this.findAppointmentSchedule(scheduleId);
        appointmentSchedule.deactivate(new CompanyId(companyId));
        appointmentSchedules.add(appointmentSchedule);
    }

    private AppointmentSchedule findAppointmentSchedule(UUID scheduleId) {
        requiresNonNull("scheduleId", scheduleId);

        return appointmentSchedules.ofId(new ScheduleId(scheduleId))
                .orElseThrow(() -> new ScheduleNotFoundException("Schedule not found"));
    }

}
