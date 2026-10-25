package br.com.ipet.ordering.domain.model.booking.appointment;

import br.com.ipet.ordering.domain.model.booking.BookingPet;
import br.com.ipet.ordering.domain.model.serviceoffering.AppointmentService;

public record PetServicePair(BookingPet bookingPet, AppointmentService service) {
}
