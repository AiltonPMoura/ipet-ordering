package br.com.ipet.ordering.application.booking.appointment.management;

import br.com.ipet.ordering.application.commons.AppointmentPetServiceInput;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record AppointmentBookingInput(UUID customerId,
                                      UUID appointmentScheduleId,
                                      UUID companyId,
                                      UUID customerAddressId,
                                      LocalDateTime requestedStart,
                                      List<AppointmentPetServiceInput> petServices,
                                      String paymentMethod) {
}
