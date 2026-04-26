package br.com.ipet.ordering.domain.model.order;

import br.com.ipet.ordering.domain.model.DomainException;

public class OrderDoesNotBelongsToTheCustomer extends DomainException {
    public OrderDoesNotBelongsToTheCustomer(String s) {
        super(s);
    }
}
