package br.com.ipet.ordering.domain.model.customer;

import br.com.ipet.ordering.domain.model.DomainException;

public class CustomerDoesNotContainPrincipalAddressException extends DomainException {
    public CustomerDoesNotContainPrincipalAddressException(String s) {
        super(s);
    }
}
