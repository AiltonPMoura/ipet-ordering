package br.com.ipet.ordering.domain.model.shoppingcart;

import br.com.ipet.ordering.domain.model.DomainException;

public class ShoppingCartDoesNotAssociatedToTheCompany extends DomainException {
    public ShoppingCartDoesNotAssociatedToTheCompany(String s) {
        super(s);
    }
}
