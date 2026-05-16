package br.com.ipet.ordering.domain.model.schedule;

import br.com.ipet.ordering.domain.model.DomainException;

public class ConflictLockedTimeException extends DomainException {
    public ConflictLockedTimeException(String message) {
        super(message);
    }
}
