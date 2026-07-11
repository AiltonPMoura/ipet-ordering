package br.com.ipet.ordering.domain.model.booking;

import br.com.ipet.ordering.domain.model.DomainException;

public class PriceCannotIsEmpty extends DomainException {
    public PriceCannotIsEmpty() {
        super("");
    }
}
