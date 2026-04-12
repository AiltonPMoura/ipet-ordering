package br.com.ipet.ordering.domain.model.shoppingcart;

import br.com.ipet.ordering.domain.model.DomainException;

public class ShoppingCartProductItemNotFoundException extends DomainException {
    public ShoppingCartProductItemNotFoundException(String s) {
        super(s);
    }
}
