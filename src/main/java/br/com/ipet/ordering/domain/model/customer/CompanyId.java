package br.com.ipet.ordering.domain.model.customer;

import br.com.ipet.ordering.domain.model.FieldValidator;
import br.com.ipet.ordering.domain.model.IdGenerator;

import java.util.UUID;

public record CompanyId(UUID value) {

    public CompanyId() {
        this(IdGenerator.generateTimeBasedEpochRandomGenerator());
    }

    public CompanyId {
        FieldValidator.requiresNonNull("companyId", value);
    }

}
