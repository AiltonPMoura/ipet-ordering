package br.com.ipet.ordering.application.schedule.query;

import br.com.ipet.ordering.domain.model.schedule.AvailableDateTimes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface AppointmentScheduleQueryService {
    AppointmentScheduleDetailOutput findById(UUID scheduleId);
    Page<AppointmentScheduleSummaryOutput> filter(AppointmentScheduleFilter filter, Pageable pageable);
    List<AvailableDateTimes> findAvailableDateTimes(UUID scheduleId);
}
