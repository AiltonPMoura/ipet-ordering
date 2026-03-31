package br.com.ipet.ordering.domain.model.shopping;

import br.com.ipet.ordering.domain.model.DomainException;

public class ShoppingCartProductItemNotFoundException extends DomainException {
    public ShoppingCartProductItemNotFoundException(String s) {
        super(s);
    }
}
