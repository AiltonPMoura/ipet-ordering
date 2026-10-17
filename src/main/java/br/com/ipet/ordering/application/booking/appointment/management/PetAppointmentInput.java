package br.com.ipet.ordering.application.booking.appointment.management;

import java.util.UUID;

public record PetAppointmentInput(UUID petId, UUID serviceOfferingId) {
}
