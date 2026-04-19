package br.com.ipet.ordering.domain.model.customer;

import br.com.ipet.ordering.domain.model.DomainException;

public class BirthDateMustBeInPast extends DomainException {
    public BirthDateMustBeInPast() {
        super("");
    }
}
