package br.com.ipet.ordering.application.booking.appointment.query;

import java.time.LocalDateTime;

public record AppointmentBookingBlockOutput(LocalDateTime scheduledStart,
                                            LocalDateTime scheduledEnd) {
}
