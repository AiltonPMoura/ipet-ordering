package br.com.ipet.ordering.domain.model.pet;

import br.com.ipet.ordering.domain.model.DomainEntityNotFoundException;
import br.com.ipet.ordering.domain.model.DomainException;
import br.com.ipet.ordering.domain.model.MessageCode;

public class PetNotFoundException extends DomainEntityNotFoundException {

    public PetNotFoundException(String value) {
        super(MessageCode.Pet.NOT_FOUND, value);
    }

}
