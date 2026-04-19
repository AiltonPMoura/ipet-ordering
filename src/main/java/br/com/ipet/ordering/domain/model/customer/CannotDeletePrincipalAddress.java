package br.com.ipet.ordering.domain.model.customer;

import br.com.ipet.ordering.domain.model.DomainException;

public class CannotDeletePrincipalAddress extends DomainException {
    public CannotDeletePrincipalAddress(String message) {
        super(message);
    }
}
