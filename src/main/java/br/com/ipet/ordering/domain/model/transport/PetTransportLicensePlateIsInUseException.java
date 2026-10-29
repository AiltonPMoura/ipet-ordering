package br.com.ipet.ordering.domain.model.transport;

import br.com.ipet.ordering.domain.model.DomainException;
import br.com.ipet.ordering.domain.model.MessageCode;

public class PetTransportLicensePlateIsInUseException extends DomainException {
    public PetTransportLicensePlateIsInUseException(String licensePlate) {
        super(MessageCode.PetTransport.LICENSE_PLATE_IN_USE, licensePlate);
    }
}
