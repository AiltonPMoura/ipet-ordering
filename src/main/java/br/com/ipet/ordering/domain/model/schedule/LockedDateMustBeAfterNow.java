package br.com.ipet.ordering.domain.model.schedule;

import br.com.ipet.ordering.domain.model.DomainException;

public class LockedDateMustBeAfterNow extends DomainException {
    public LockedDateMustBeAfterNow(String message) {
        super(message);
    }
}
