package br.com.ipet.ordering.domain.model.schedule.appointment;

import br.com.ipet.ordering.domain.model.DomainException;
import br.com.ipet.ordering.domain.model.MessageCode;

public class InvalidIntervalException extends DomainException {

    public InvalidIntervalException() {
        super(MessageCode.ERROR_INVALID_INTERVAL_LOCKED_TIME);
    }

}
