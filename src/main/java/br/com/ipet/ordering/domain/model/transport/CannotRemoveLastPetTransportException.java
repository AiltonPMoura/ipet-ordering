package br.com.ipet.ordering.domain.model.transport;

import br.com.ipet.ordering.domain.model.DomainException;
import br.com.ipet.ordering.domain.model.MessageCode;

public class CannotRemoveLastPetTransportException extends DomainException {

    public CannotRemoveLastPetTransportException() {
        super(MessageCode.PetTransport.CANNOT_REMOVE_LAST);
    }
}
