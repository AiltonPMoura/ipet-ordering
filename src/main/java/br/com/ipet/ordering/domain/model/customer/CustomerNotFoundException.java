package br.com.ipet.ordering.domain.model.customer;

import br.com.ipet.ordering.domain.model.exception.DomainException;

public class CustomerNotFoundException extends DomainException {
    public CustomerNotFoundException() {
        super("message");
    }
}
