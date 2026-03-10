package br.com.ipet.ordering.domain.model.customer;

import br.com.ipet.ordering.domain.model.DomainException;

public class CustomerDoesNotContainAnyPet extends DomainException {
    public CustomerDoesNotContainAnyPet() {
        super("");
    }
}
