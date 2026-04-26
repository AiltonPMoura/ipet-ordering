package br.com.ipet.ordering.domain.model.order;

import br.com.ipet.ordering.domain.model.DomainException;

public class OrderDoesNotAssociatedWithCompany extends DomainException {
    public OrderDoesNotAssociatedWithCompany(String s) {
        super(s);
    }
}
