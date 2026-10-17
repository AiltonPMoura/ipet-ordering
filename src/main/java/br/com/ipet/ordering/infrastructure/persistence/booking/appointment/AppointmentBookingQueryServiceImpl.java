package br.com.ipet.ordering.infrastructure.persistence.booking.appointment;

import br.com.ipet.ordering.application.booking.appointment.query.AppointmentBookingBlockOutput;
import br.com.ipet.ordering.application.booking.appointment.query.AppointmentBookingQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AppointmentBookingQueryServiceImpl implements AppointmentBookingQueryService {

    private final AppointmentBookingPersistenceRepository repository;

    @Override
    public List<AppointmentBookingBlockOutput> findBlockedBookings(
            UUID scheduleId, LocalDateTime windowStart, LocalDateTime windowEnd, LocalDateTime gracePeriod) {
        return repository.findBlockedBookings(scheduleId, windowStart, windowEnd, gracePeriod);
    }
}
