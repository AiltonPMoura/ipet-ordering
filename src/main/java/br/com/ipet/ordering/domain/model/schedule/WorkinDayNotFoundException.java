package br.com.ipet.ordering.domain.model.schedule;

import br.com.ipet.ordering.domain.model.DomainException;

public class WorkinDayNotFoundException extends DomainException {
    public WorkinDayNotFoundException(String message) {
        super(message);
    }
}
