package br.com.ipet.ordering.domain.model.scheduling;

import br.com.ipet.ordering.domain.model.DomainException;

public class PetCannotBeChangeInThisStatusException extends DomainException {
    public PetCannotBeChangeInThisStatusException(String status) {
        super("Pet cannot be changed when scheduling is in status: " + status);
    }
}
