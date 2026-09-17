package br.com.ipet.ordering.presentation.schedule.appointment;

import br.com.ipet.ordering.application.schedule.ScheduleInput;
import br.com.ipet.ordering.application.schedule.appointment.management.AppointmentScheduleApplicationService;
import br.com.ipet.ordering.application.schedule.appointment.query.AppointmentScheduleDetailOutput;
import br.com.ipet.ordering.application.schedule.appointment.query.AppointmentScheduleFilter;
import br.com.ipet.ordering.application.schedule.appointment.query.AppointmentScheduleQueryService;
import br.com.ipet.ordering.application.schedule.appointment.query.AppointmentScheduleSummaryOutput;
import br.com.ipet.ordering.domain.model.company.CompanyId;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/appointment-schedules")
@RequiredArgsConstructor
public class AppointmentScheduleController {

    private final AppointmentScheduleApplicationService appointmentScheduleApplicationService;
    private final AppointmentScheduleQueryService appointmentScheduleQueryService;

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody ScheduleInput input) {
        var appointmentScheduleId = appointmentScheduleApplicationService.create(input);

        var url = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(appointmentScheduleId)
                .toUri();

        return ResponseEntity.created(url).build();
    }

    @PatchMapping("/{id}/name")
    public void changeName(@PathVariable UUID id, String name) {
        appointmentScheduleApplicationService.changeName(id, new CompanyId().value(), name);
    }

    @PatchMapping("/{id}/locked-dates")
    public void changeLockedDates(@PathVariable UUID id, @RequestBody List<LocalDate> lockedDates) {
        appointmentScheduleApplicationService.changeLockedDates(id, new CompanyId().value(), lockedDates);
    }

    @GetMapping("/{id}")
    public AppointmentScheduleDetailOutput findById(@PathVariable UUID id) {
        return appointmentScheduleQueryService.findById(id);
    }

    @PatchMapping("/{id}/active")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void active(@PathVariable UUID id) {
        appointmentScheduleApplicationService.active(id, new CompanyId().value());
    }

    @PatchMapping("/{id}/inactive")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void inactive(@PathVariable UUID id) {
        appointmentScheduleApplicationService.inactive(id, new CompanyId().value());
    }

}

