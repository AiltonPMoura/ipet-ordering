package br.com.ipet.ordering.application.booking.appointment.management;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record AppointmentBookingInput(UUID customerId,
                                      UUID appointmentScheduleId,
                                      UUID companyId,
                                      LocalDateTime requestedStart,
                                      List<AppointmentPetServiceInput> appointments) {
}
