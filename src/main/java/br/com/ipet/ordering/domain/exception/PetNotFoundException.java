package br.com.ipet.ordering.domain.exception;

import br.com.ipet.ordering.domain.exception.message.MessageCode;

public class PetNotFoundException extends DomainException {

    private final String[] fields;

    public PetNotFoundException(String... fields) {
        super(MessageCode.ERROR_PET_NOT_FOUND);
        this.fields = fields;
    }
}
