package br.com.ipet.ordering.domain.model.pet;

import br.com.ipet.ordering.domain.model.exception.DomainException;

public class PetDoesNotBelongToTheCustomer extends DomainException {
    public PetDoesNotBelongToTheCustomer() {
        super("message");
    }
}
