package br.com.ipet.ordering.domain.model.commons.valueobject;

import br.com.ipet.ordering.domain.model.FieldValidator;

public record Billing(Customer customer) {

    public Billing {
        FieldValidator.requiresNonNull("customer", customer);
    }

}
