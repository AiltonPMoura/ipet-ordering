package br.com.ipet.ordering.application.booking.appointment.management;

import java.util.UUID;

public record AppointmentPetServiceInput(UUID petId, UUID serviceId) {
}
