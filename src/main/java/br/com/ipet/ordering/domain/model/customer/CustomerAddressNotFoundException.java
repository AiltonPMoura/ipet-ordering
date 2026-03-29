package br.com.ipet.ordering.domain.model.customer;

import br.com.ipet.ordering.domain.model.DomainException;

public class CustomerAddressNotFoundException extends DomainException {
    public CustomerAddressNotFoundException(String s) {
        super(s);
    }
}
