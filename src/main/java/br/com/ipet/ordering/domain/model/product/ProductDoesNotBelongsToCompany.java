package br.com.ipet.ordering.domain.model.product;

import br.com.ipet.ordering.domain.model.DomainException;

public class ProductDoesNotBelongsToCompany extends DomainException {
    public ProductDoesNotBelongsToCompany(String s) {
        super(s);
    }
}
