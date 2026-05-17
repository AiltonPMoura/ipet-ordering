package br.com.ipet.ordering.domain.model.schedule;

import br.com.ipet.ordering.domain.model.DomainException;

public class CannotAddLockedDateInStatusInactived extends DomainException {
    public CannotAddLockedDateInStatusInactived(String s) {
        super(s);
    }
}
