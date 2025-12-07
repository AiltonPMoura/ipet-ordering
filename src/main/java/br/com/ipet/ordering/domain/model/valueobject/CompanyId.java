package br.com.ipet.ordering.domain.model.valueobject;

import br.com.ipet.ordering.domain.model.util.FieldValidator;
import br.com.ipet.ordering.domain.model.util.IdGenerator;

import java.util.UUID;

public record CompanyId(UUID value) {

    public CompanyId() {
        this(IdGenerator.generateTimeBasedEpochRandomGenerator());
    }

    public CompanyId {
        FieldValidator.requiresNonNull("companyId", value);
    }

}
