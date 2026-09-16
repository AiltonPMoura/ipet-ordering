package br.com.ipet.ordering.domain.model.schedule.appointment;

import br.com.ipet.ordering.domain.model.DomainException;

public class WorkDayAlreadyExistsException extends DomainException {
    public WorkDayAlreadyExistsException() {
        super("");
    }
}
