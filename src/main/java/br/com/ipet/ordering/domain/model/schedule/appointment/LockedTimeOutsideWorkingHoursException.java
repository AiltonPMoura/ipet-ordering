package br.com.ipet.ordering.domain.model.schedule.appointment;

import br.com.ipet.ordering.domain.model.DomainException;

public class LockedTimeOutsideWorkingHoursException extends DomainException {

    public LockedTimeOutsideWorkingHoursException(String message) {
        super(message);
    }
}
