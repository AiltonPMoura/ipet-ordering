package br.com.ipet.ordering.domain.model.customer;

import br.com.ipet.ordering.domain.model.DomainException;

public class CustomerEmailInUseException extends DomainException {
    public CustomerEmailInUseException() {
        super("invalid email, already in use");
    }
}
