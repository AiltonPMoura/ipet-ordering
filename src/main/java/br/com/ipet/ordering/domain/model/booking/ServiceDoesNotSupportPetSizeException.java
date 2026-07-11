package br.com.ipet.ordering.domain.model.booking;

import br.com.ipet.ordering.domain.model.DomainException;

public class ServiceDoesNotSupportPetSizeException extends DomainException {
    public ServiceDoesNotSupportPetSizeException(String petSize, String serviceSize) {
        super("Service does not support pet size. Service: %s, Pet size: %s".formatted(petSize, serviceSize));
    }
}
