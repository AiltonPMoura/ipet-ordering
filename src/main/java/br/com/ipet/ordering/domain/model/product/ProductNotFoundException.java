package br.com.ipet.ordering.domain.model.product;

import br.com.ipet.ordering.domain.model.DomainException;

public class ProductNotFoundException extends DomainException {
    public ProductNotFoundException(String s) {
        super(s);
    }
}
