package br.com.ipet.ordering.domain.model.pet;

import br.com.ipet.ordering.domain.model.DomainException;

public class AgeCannotBeNegativeException extends DomainException {
    public AgeCannotBeNegativeException() {
        super("message");
    }
}
