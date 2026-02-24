package br.com.ipet.ordering.domain.model.schedule;

import br.com.ipet.ordering.domain.model.DomainException;

public class ScheduleAlreadyExistsException extends DomainException {
    public ScheduleAlreadyExistsException() {
        super("");
    }
}
