package br.com.ipet.ordering.domain.model.booking.appointment;

import br.com.ipet.ordering.domain.model.Repository;
import br.com.ipet.ordering.domain.model.booking.BookingId;
import br.com.ipet.ordering.domain.model.commons.id.ScheduleId;

import java.time.LocalDateTime;

public interface AppointmentBookings extends Repository<AppointmentBooking, BookingId> {
    boolean existsTimeSlotConflict(ScheduleId scheduleId, LocalDateTime requestedStart, LocalDateTime requestedEnd);
}
