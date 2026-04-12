package br.com.ipet.ordering.domain.model.shoppingcart;

import br.com.ipet.ordering.domain.model.DomainException;

public class CustomerAlreadyHaveShoppingCartException extends DomainException {
    public CustomerAlreadyHaveShoppingCartException(String s) {
        super(s);
    }
}
