package br.com.ipet.ordering.domain.model.schedule;

import br.com.ipet.ordering.domain.model.DomainException;

public class CannotAddLockedDateInStatusDraft extends DomainException {
    public CannotAddLockedDateInStatusDraft(String message) {
        super(message);
    }
}
