package br.com.ipet.ordering.domain.model.transport;

import br.com.ipet.ordering.domain.model.DomainException;
import br.com.ipet.ordering.domain.model.MessageCode;

public class LicensePlateIsNotValidException extends DomainException {
    public LicensePlateIsNotValidException(String value) {
        super(MessageCode.PetTransport.INVALID_LICENSE_PLATE, value);
    }
}
