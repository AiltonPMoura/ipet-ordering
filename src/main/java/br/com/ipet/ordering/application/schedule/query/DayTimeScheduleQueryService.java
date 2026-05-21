package br.com.ipet.ordering.application.schedule.query;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface DayTimeScheduleQueryService {
    DayTimeScheduleDetailOutput findById(UUID scheduleId);
    Page<DayTimeScheduleSummaryOutput> filter(DayTimeScheduleFilter filter, Pageable pageable);
}
