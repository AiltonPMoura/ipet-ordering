package br.com.ipet.ordering.application.booking.appointment.query;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public interface AppointmentBookingQueryService {

    List<AppointmentBookingBlockOutput> findBlockedBookings(
            UUID scheduleId,
            LocalDateTime windowStart,
            LocalDateTime windowEnd,
            LocalDateTime gracePeriod
    );

}
