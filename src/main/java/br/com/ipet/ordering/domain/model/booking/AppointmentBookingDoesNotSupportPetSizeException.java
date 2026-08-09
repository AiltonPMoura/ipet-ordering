package br.com.ipet.ordering.domain.model.booking;

import br.com.ipet.ordering.domain.model.DomainException;

public class AppointmentBookingDoesNotSupportPetSizeException extends DomainException {
    public AppointmentBookingDoesNotSupportPetSizeException(String petSize, String serviceSize) {
        super("Service does not support pet size. Service: %s, Pet size: %s".formatted(petSize, serviceSize));
    }
}
