package br.com.ipet.ordering.domain.model.agenda;

import br.com.ipet.ordering.domain.model.DomainException;

public class ScheduleAlreadyExistsException extends DomainException {
    public ScheduleAlreadyExistsException() {
        super("");
    }
}
