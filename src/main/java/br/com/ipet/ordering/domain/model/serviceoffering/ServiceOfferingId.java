package br.com.ipet.ordering.domain.model.serviceoffering;

import br.com.ipet.ordering.domain.model.FieldValidator;

import java.util.UUID;

public record ServiceOfferingId(UUID value) {

    public ServiceOfferingId {
        FieldValidator.requiresNonNull("serviceOfferingId value", value);
    }

    @Override
    public String toString() {
        return this.value.toString();
    }

}
