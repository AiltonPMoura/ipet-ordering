package br.com.ipet.ordering.domain.valueobject;

import br.com.ipet.ordering.domain.util.FieldValidator;
import br.com.ipet.ordering.domain.util.IdGenerator;

import java.util.UUID;

public record ServiceId(UUID value) {

    public ServiceId() {
        this(IdGenerator.generateTimeBasedEpochRandomGenerator());
    }

    public ServiceId {
        FieldValidator.requiresNonNull("serviceId value", value);
    }

}
