package br.com.ipet.ordering.domain.model.booking.appointment;

import br.com.ipet.ordering.domain.model.booking.Pet;
import br.com.ipet.ordering.domain.model.serviceoffering.AppointmentService;

public record PetServicePair(Pet pet, AppointmentService service) {
}
