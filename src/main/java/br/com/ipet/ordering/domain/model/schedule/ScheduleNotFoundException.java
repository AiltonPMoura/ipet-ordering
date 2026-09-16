package br.com.ipet.ordering.domain.model.schedule;

import br.com.ipet.ordering.domain.model.DomainException;

public class ScheduleNotFoundException extends DomainException {
    public ScheduleNotFoundException(String message) {
        super(message);
    }
}
