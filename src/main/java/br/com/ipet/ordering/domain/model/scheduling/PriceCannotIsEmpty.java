package br.com.ipet.ordering.domain.model.scheduling;

import br.com.ipet.ordering.domain.model.DomainException;

public class PriceCannotIsEmpty extends DomainException {
    public PriceCannotIsEmpty() {
        super("");
    }
}
