package br.com.ipet.ordering.domain.model.order;

import br.com.ipet.ordering.domain.model.FieldValidator;
import br.com.ipet.ordering.domain.model.commons.valueobject.Company;

public record DeliveryCompany(Company company) {

    public DeliveryCompany {
        FieldValidator.requiresNonNull("company", company);
    }
}