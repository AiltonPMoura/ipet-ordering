package br.com.ipet.ordering.domain.model.valueobject;

import br.com.ipet.ordering.domain.model.util.FieldValidator;
import br.com.ipet.ordering.domain.model.util.IdGenerator;

import java.util.UUID;

public record CustumerId(UUID value) {

    public CustumerId {
        FieldValidator.requiresNonNull("customerId value", value);
    }

    public CustumerId() {
        this(IdGenerator.generateTimeBasedEpochRandomGenerator());
    }

}
