package br.com.ipet.ordering.domain.model.customer;

import br.com.ipet.ordering.domain.model.DomainEntityNotFoundException;
import br.com.ipet.ordering.domain.model.MessageCode;

public class CustomerNotFoundException extends DomainEntityNotFoundException {
    public CustomerNotFoundException(String value) {
        super(MessageCode.Customer.NOT_FOUND, value);
    }
}
