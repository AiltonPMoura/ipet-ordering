package br.com.ipet.ordering.domain.model.product;

import br.com.ipet.ordering.domain.model.DomainException;

public class ProductDoesNotBelongsToTheCompany extends DomainException {
    public ProductDoesNotBelongsToTheCompany(String s) {
        super(s);
    }
}
