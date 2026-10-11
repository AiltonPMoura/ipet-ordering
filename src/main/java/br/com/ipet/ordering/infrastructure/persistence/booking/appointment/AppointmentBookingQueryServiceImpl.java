package br.com.ipet.ordering.infrastructure.persistence.booking.appointment;

import br.com.ipet.ordering.application.booking.appointment.AppointmentBookingBlockOutput;
import br.com.ipet.ordering.application.booking.appointment.AppointmentBookingQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AppointmentBookingQueryServiceImpl implements AppointmentBookingQueryService {

    private final AppointmentBookingPersistenceRepository repository;

    @Override
    public List<AppointmentBookingBlockOutput> findBlockedBookings(
            UUID scheduleId, OffsetDateTime windowStart, OffsetDateTime windowEnd, OffsetDateTime gracePeriod) {
        return repository.findBlockedBookings(scheduleId, windowStart, windowEnd, gracePeriod);
    }
}
