package br.com.ipet.ordering.domain.model.schedule;

import br.com.ipet.ordering.domain.model.DomainException;
import br.com.ipet.ordering.domain.model.MessageCode;

public class WorkingHoursCanNotBeLessThanOneHourException extends DomainException {

    public WorkingHoursCanNotBeLessThanOneHourException() {
        super(MessageCode.ERROR_WORKING_HOURS_CANNOT_BE_LESS_THAN_ONE);
    }

}
