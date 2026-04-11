package br.com.ipet.ordering.domain.model.order;

import br.com.ipet.ordering.domain.model.DomainException;

public class ShoppingCartCantProceedToCheckoutException extends DomainException {
    public ShoppingCartCantProceedToCheckoutException(String s) {
        super(s);
    }
}
