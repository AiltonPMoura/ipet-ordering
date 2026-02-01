package br.com.ipet.ordering.domain.model.pet;

import br.com.ipet.ordering.domain.model.DomainException;

public class PetWeightCannoeBeNegativeException extends DomainException {
    public PetWeightCannoeBeNegativeException() {
        super("message");
    }
}
