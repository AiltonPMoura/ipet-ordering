package br.com.ipet.ordering.domain.model.transport;

import br.com.ipet.ordering.domain.model.DomainEntityNotFoundException;
import br.com.ipet.ordering.domain.model.MessageCode;

public class PetTransportNotFoundException extends DomainEntityNotFoundException {

    public PetTransportNotFoundException(String value) {
        super(MessageCode.PetTransport.NOT_FOUND, value);
    }
}
