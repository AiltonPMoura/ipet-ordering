package br.com.ipet.ordering.presentation.schedule.appointment;

import br.com.ipet.ordering.application.commons.NameInput;
import br.com.ipet.ordering.application.schedule.ScheduleInput;
import br.com.ipet.ordering.application.schedule.appointment.management.AppointmentScheduleApplicationService;
import br.com.ipet.ordering.application.schedule.appointment.query.AppointmentScheduleDetailOutput;
import br.com.ipet.ordering.application.schedule.appointment.query.AppointmentScheduleQueryService;
import br.com.ipet.ordering.domain.model.company.CompanyId;
import br.com.ipet.ordering.domain.model.schedule.ScheduleAvailabilityOutput;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

    private final AppointmentScheduleApplicationService applicationService;
    private final AppointmentScheduleQueryService queryService;

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody ScheduleInput input) {
        var appointmentScheduleId = applicationService.create(input);

        var url = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{appointmentScheduleId}")
                .buildAndExpand(appointmentScheduleId)
                .toUri();

        return ResponseEntity.created(url).build();
    }

    @PutMapping("/{appointmentScheduleId}/name")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changeName(@PathVariable UUID appointmentScheduleId, @RequestBody NameInput name) {
        applicationService.changeName(appointmentScheduleId, new CompanyId().value(), name);
    }

    @PutMapping("/{appointmentScheduleId}/blocked-dates")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changeLockedDates(@PathVariable UUID appointmentScheduleId, @RequestBody List<LocalDate> blockedDates) {
        applicationService.changeBlockedDates(appointmentScheduleId, new CompanyId().value(), blockedDates);
    }

    @GetMapping("/{appointmentScheduleId}")
    public AppointmentScheduleDetailOutput findById(@PathVariable UUID appointmentScheduleId) {
        return queryService.findById(appointmentScheduleId);
    }

    @GetMapping("/{companyId}/availability")
    public ScheduleAvailabilityOutput findScheduleAvailabilityByCompanyId(@PathVariable UUID companyId) {
        return queryService.findScheduleAvailability(companyId);
    }

    @PostMapping("/{appointmentScheduleId}/activate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void activate(@PathVariable UUID appointmentScheduleId) {
        applicationService.activate(appointmentScheduleId, new CompanyId().value());
    }

    @PostMapping("/{appointmentScheduleId}/deactivate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deactivate(@PathVariable UUID appointmentScheduleId) {
        applicationService.deactivate(appointmentScheduleId, new CompanyId().value());
    }

}

