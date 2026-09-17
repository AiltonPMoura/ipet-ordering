package br.com.ipet.ordering.presentation.schedule.appointment;

import br.com.ipet.ordering.application.schedule.appointment.management.AppointmentScheduleApplicationService;
import br.com.ipet.ordering.application.schedule.appointment.management.WorkDayInput;
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
@RequestMapping("/v1/appointment-schedules/{appointmentScheduleId}/work-days")
@RequiredArgsConstructor
public class WorkDayController {

    private final AppointmentScheduleApplicationService appointmentScheduleApplicationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@PathVariable UUID appointmentScheduleId, @RequestBody WorkDayInput input) {
        appointmentScheduleApplicationService.addWorkDay(appointmentScheduleId, new CompanyId().value(), input);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID appointmentScheduleId, @PathVariable UUID id) {
        appointmentScheduleApplicationService.removeWorkDay(appointmentScheduleId, new CompanyId().value(), id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable UUID appointmentScheduleId, @PathVariable UUID id, @RequestBody WorkDayInput input) {
        appointmentScheduleApplicationService.updateWorkDay(appointmentScheduleId, new CompanyId().value(), id, input);
    }

}
