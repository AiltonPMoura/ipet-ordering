package br.com.ipet.ordering.domain.model.shoppingcart;

import br.com.ipet.ordering.domain.model.DomainException;

public class ShoppingCartNotFoundException extends DomainException {
    public ShoppingCartNotFoundException(String s) {
        super(s);
    }
}
