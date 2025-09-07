package br.com.ipet.ordering.domain.valueobject;

import br.com.ipet.ordering.domain.util.FieldValidator;
import br.com.ipet.ordering.domain.util.IdGenerator;

import java.util.UUID;

public record CustumerId(UUID value) {

    public CustumerId {
        FieldValidator.notNull("customerId value", value);
    }

    public CustumerId() {
        this(IdGenerator.generateTimeBasedEpochRandomGenerator());
    }

}
