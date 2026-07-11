package br.com.ipet.ordering.presentation;

import br.com.ipet.ordering.application.schedule.management.ScheduleInput;
import br.com.ipet.ordering.application.schedule.management.daytime.DayTimeScheduleApplicationService;
import br.com.ipet.ordering.application.schedule.query.DayTimeScheduleDetailOutput;
import br.com.ipet.ordering.application.schedule.query.DayTimeScheduleQueryService;
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
@RequestMapping("/day-time-schedules")
@RequiredArgsConstructor
public class DayTimeScheduleController {

    private final DayTimeScheduleApplicationService dayTimeScheduleApplication;
    private final DayTimeScheduleQueryService dayTimeScheduleQuery;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UUID create(@RequestBody ScheduleInput input) {
        return dayTimeScheduleApplication.create(input);
    }

    @GetMapping("/{id}")
    public DayTimeScheduleDetailOutput findById(@PathVariable UUID id) {
        return dayTimeScheduleQuery.findById(id);
    }

}
