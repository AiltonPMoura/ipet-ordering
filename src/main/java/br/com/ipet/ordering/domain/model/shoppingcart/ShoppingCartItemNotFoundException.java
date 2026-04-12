package br.com.ipet.ordering.domain.model.shoppingcart;

import br.com.ipet.ordering.domain.model.DomainException;

public class ShoppingCartItemNotFoundException extends DomainException {
    public ShoppingCartItemNotFoundException(String message) {
        super(message);
    }
}
