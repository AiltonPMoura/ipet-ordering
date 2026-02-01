package br.com.ipet.ordering.domain.model.pet;

import br.com.ipet.ordering.domain.model.DomainException;

public class PetDoesNotBelongToTheCustomer extends DomainException {
    public PetDoesNotBelongToTheCustomer() {
        super("message");
    }
}
