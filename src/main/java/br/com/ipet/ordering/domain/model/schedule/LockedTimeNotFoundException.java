package br.com.ipet.ordering.domain.model.schedule;

import br.com.ipet.ordering.domain.model.DomainException;

public class LockedTimeNotFoundException extends DomainException {
    public LockedTimeNotFoundException() {
        super("");
    }
}
