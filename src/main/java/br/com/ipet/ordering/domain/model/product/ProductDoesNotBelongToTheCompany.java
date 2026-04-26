package br.com.ipet.ordering.domain.model.product;

import br.com.ipet.ordering.domain.model.DomainException;

public class ProductDoesNotBelongToTheCompany extends DomainException {
    public ProductDoesNotBelongToTheCompany(String s) {
        super(s);
    }
}
