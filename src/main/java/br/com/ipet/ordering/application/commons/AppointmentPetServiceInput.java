package br.com.ipet.ordering.application.commons;

import java.util.UUID;

public record AppointmentPetServiceInput(UUID petId, UUID serviceId) {
}
