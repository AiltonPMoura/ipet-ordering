package br.com.ipet.ordering.presentation.schedule.appointment;

import br.com.ipet.ordering.application.schedule.appointment.management.AppointmentScheduleApplicationService;
import br.com.ipet.ordering.application.schedule.appointment.management.LockedTimeInput;
import br.com.ipet.ordering.application.schedule.appointment.management.LockedTimeUpdateInput;
import br.com.ipet.ordering.domain.model.company.CompanyId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/v1/appointment-schedules/{appointmentScheduleId}/work-days/{workDayId}/locked-times")
@RequiredArgsConstructor
public class LockedTimeController {

    private final AppointmentScheduleApplicationService appointmentScheduleApplicationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@PathVariable UUID appointmentScheduleId,
                       @PathVariable UUID workDayId,
                       @RequestBody LockedTimeInput input) {
        appointmentScheduleApplicationService.addLockedTime(appointmentScheduleId, workDayId, new CompanyId().value(), input);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID appointmentScheduleId,
                       @PathVariable UUID workDayId,
                       @RequestBody LockedTimeInput input) {
        appointmentScheduleApplicationService.removeLockedTime(appointmentScheduleId, workDayId, new CompanyId().value(), input);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void change(@PathVariable UUID appointmentScheduleId,
                       @PathVariable UUID workDayId,
                       @RequestBody LockedTimeUpdateInput input) {
        appointmentScheduleApplicationService.changeLockedTime(appointmentScheduleId, workDayId, new CompanyId().value(),
                input.oldLockedTime(), input.newLockedTime());
    }

}
