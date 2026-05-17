package br.com.ipet.ordering.domain.model.shoppingcart;

import br.com.ipet.ordering.domain.model.DomainException;

public class ShoppingCartDoesNotBelongToCustomer extends DomainException {
    public ShoppingCartDoesNotBelongToCustomer(String s) {
        super(s);
    }
}
