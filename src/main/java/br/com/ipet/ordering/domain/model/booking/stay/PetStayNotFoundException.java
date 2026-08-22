package br.com.ipet.ordering.domain.model.booking.stay;

import br.com.ipet.ordering.domain.model.DomainException;

public class PetStayNotFoundException extends DomainException {
    public PetStayNotFoundException(String string, String string1) {
        super(string, string1);
    }
}
