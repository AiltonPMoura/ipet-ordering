package br.com.ipet.ordering.domain.model.commons.valueobject;

import br.com.ipet.ordering.domain.model.DomainException;

public class QuantityCannotBeNegative extends DomainException {
    public QuantityCannotBeNegative() {
        super("");
    }
}
