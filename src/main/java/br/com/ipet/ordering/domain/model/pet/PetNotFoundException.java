package br.com.ipet.ordering.domain.model.pet;

import br.com.ipet.ordering.domain.model.DomainException;
import br.com.ipet.ordering.domain.model.MessageCode;

public class PetNotFoundException extends DomainException {

    private final String[] fields;

    public PetNotFoundException(String... fields) {
        super(MessageCode.ERROR_PET_NOT_FOUND);
        this.fields = fields;
    }
}
