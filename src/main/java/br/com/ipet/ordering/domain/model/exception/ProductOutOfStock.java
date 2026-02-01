package br.com.ipet.ordering.domain.model.exception;

import br.com.ipet.ordering.domain.model.DomainException;

public class ProductOutOfStock extends DomainException {
    public ProductOutOfStock() {
        super("");
    }
}
