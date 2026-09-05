package br.com.ipet.ordering.presentation;

import br.com.ipet.ordering.application.schedule.management.ScheduleInput;
import br.com.ipet.ordering.application.schedule.management.daytime.AppointmentScheduleApplicationService;
import br.com.ipet.ordering.application.schedule.query.AppointmentScheduleDetailOutput;
import br.com.ipet.ordering.application.schedule.query.AppointmentScheduleQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/appointment-schedules")
@RequiredArgsConstructor
public class AppointmentScheduleController {

    private final AppointmentScheduleApplicationService appointmentScheduleApplicationService;
    private final AppointmentScheduleQueryService appointmentScheduleQueryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UUID create(@RequestBody ScheduleInput input) {
        return appointmentScheduleApplicationService.create(input);
    }

    @GetMapping("/{id}")
    public AppointmentScheduleDetailOutput findById(@PathVariable UUID id) {
        return appointmentScheduleQueryService.findById(id);
    }

}
