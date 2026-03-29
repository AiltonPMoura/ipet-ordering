package br.com.ipet.ordering.domain.model.customer;

import br.com.ipet.ordering.domain.model.FieldValidator;
import br.com.ipet.ordering.domain.model.IdGenerator;

import java.util.UUID;

public record CustomerAddressId(UUID value) {

    public CustomerAddressId {
        FieldValidator.requiresNonNull("id", value);
    }

    public CustomerAddressId() {
        this(IdGenerator.generateTimeBasedEpochRandomGenerator());
    }

}
