package br.com.ipet.ordering.application.booking.appointment;

import java.time.OffsetDateTime;

public record AppointmentBookingBlockOutput(OffsetDateTime startDate,
                                            OffsetDateTime endDate) {
}
