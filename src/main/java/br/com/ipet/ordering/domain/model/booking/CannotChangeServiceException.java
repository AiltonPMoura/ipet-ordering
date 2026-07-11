package br.com.ipet.ordering.domain.model.booking;

import br.com.ipet.ordering.domain.model.DomainException;

public class CannotChangeServiceException extends DomainException {
    public CannotChangeServiceException(String currentPetSize, String newServicePetSize) {
        super("Cannot change service. The current pet size (" + currentPetSize + ") does not is same as the new service pet size (" + newServicePetSize + ").");
    }
}
