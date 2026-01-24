package br.com.ipet.ordering.domain.model.commons;

import br.com.ipet.ordering.domain.model.util.FieldValidator;
import br.com.ipet.ordering.domain.model.util.IdGenerator;

import java.util.UUID;

public record ServiceId(UUID value) {

    public ServiceId() {
        this(IdGenerator.generateTimeBasedEpochRandomGenerator());
    }

    public ServiceId {
        FieldValidator.requiresNonNull("serviceId value", value);
    }

}
