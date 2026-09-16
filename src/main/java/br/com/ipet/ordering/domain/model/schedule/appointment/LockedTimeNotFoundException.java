package br.com.ipet.ordering.domain.model.schedule.appointment;

import br.com.ipet.ordering.domain.model.DomainException;

public class LockedTimeNotFoundException extends DomainException {
    public LockedTimeNotFoundException(String message) {
        super(message);
    }
}
