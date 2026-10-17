package br.com.ipet.ordering.domain.model.serviceoffering;

import br.com.ipet.ordering.domain.model.FieldValidator;

import java.util.UUID;

public record ServiceId(UUID value) {

    public ServiceId {
        FieldValidator.requiresNonNull("serviceId value", value);
    }

    @Override
    public String toString() {
        return this.value.toString();
    }

}
