package br.com.ipet.ordering.application.booking.appointment;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public interface AppointmentBookingQueryService {

    List<AppointmentBookingBlockOutput> findBlockedBookings(
            UUID scheduleId,
            OffsetDateTime windowStart,
            OffsetDateTime windowEnd,
            OffsetDateTime gracePeriod
    );

}
