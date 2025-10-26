package br.com.ipet.ordering.domain.valueobject;

import br.com.ipet.ordering.domain.util.FieldValidator;
import br.com.ipet.ordering.domain.util.IdGenerator;

import java.util.UUID;

public record CompanyId(UUID value) {

    public CompanyId() {
        this(IdGenerator.generateTimeBasedEpochRandomGenerator());
    }

    public CompanyId {
        FieldValidator.requiresNonNull("companyId value", value);
    }

}
