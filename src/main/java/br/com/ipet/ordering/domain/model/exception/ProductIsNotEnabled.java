package br.com.ipet.ordering.domain.model.exception;

import br.com.ipet.ordering.domain.model.DomainException;

public class ProductIsNotEnabled extends DomainException {
    public ProductIsNotEnabled() {
        super("");
    }
}
