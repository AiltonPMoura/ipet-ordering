package br.com.ipet.ordering.domain.model.shoppingcart;

import br.com.ipet.ordering.domain.model.DomainException;

public class ShoppingCartDoesNotBelongToTheCustomer extends DomainException {
    public ShoppingCartDoesNotBelongToTheCustomer(String s) {
        super(s);
    }
}
