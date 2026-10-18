package br.com.ipet.ordering.application.schedule.appointment.query;

import br.com.ipet.ordering.domain.model.schedule.ScheduleAvailabilityOutput;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface AppointmentScheduleQueryService {
    AppointmentScheduleDetailOutput findById(UUID id);
    AppointmentScheduleDetailOutput findByCompanyId(UUID companyId);
    Page<AppointmentScheduleSummaryOutput> filter(AppointmentScheduleFilter filter, Pageable pageable);
    ScheduleAvailabilityOutput findScheduleAvailability(UUID scheduleId);
}
