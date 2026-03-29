package br.com.ipet.ordering.domain.model.customer;

import br.com.ipet.ordering.domain.model.DomainException;

public class CannotDeleteDeliveryAddress extends DomainException {
    public CannotDeleteDeliveryAddress(String message) {
        super(message);
    }
}
