package br.com.ipet.ordering.domain.model.booking.stay;

import br.com.ipet.ordering.domain.model.DomainException;

public class StayBookingDoesNotSupportPetSizeException extends DomainException {
    public StayBookingDoesNotSupportPetSizeException(String size, String s) {
        super(size, s);
    }
}
