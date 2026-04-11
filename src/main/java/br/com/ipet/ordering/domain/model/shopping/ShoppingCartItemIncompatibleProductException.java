package br.com.ipet.ordering.domain.model.shopping;

import br.com.ipet.ordering.domain.model.DomainException;

public class ShoppingCartItemIncompatibleProductException extends DomainException {
    public ShoppingCartItemIncompatibleProductException(String s) {
        super(s);
    }
}
