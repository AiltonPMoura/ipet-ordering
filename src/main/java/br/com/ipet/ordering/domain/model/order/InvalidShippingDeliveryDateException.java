package br.com.ipet.ordering.domain.model.order;

import br.com.ipet.ordering.domain.model.DomainException;

public class InvalidShippingDeliveryDateException extends DomainException {
    public InvalidShippingDeliveryDateException(String message) {
        super("message");
    }
}
