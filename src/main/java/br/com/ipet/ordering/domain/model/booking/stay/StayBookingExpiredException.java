package br.com.ipet.ordering.domain.model.booking.stay;

import br.com.ipet.ordering.domain.model.DomainException;

public class StayBookingExpiredException extends DomainException {
    public StayBookingExpiredException(String s) {
        super(s);
    }
}
