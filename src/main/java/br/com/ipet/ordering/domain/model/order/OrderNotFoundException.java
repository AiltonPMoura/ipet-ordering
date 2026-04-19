package br.com.ipet.ordering.domain.model.order;

import br.com.ipet.ordering.domain.model.DomainException;

public class OrderNotFoundException extends DomainException {
    public OrderNotFoundException(String s) {
        super(s);
    }
}
