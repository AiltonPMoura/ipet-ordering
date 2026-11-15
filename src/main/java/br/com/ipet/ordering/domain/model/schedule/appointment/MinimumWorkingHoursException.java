package br.com.ipet.ordering.domain.model.schedule.appointment;

import br.com.ipet.ordering.domain.model.DomainException;
import br.com.ipet.ordering.domain.model.MessageCode;

public class MinimumWorkingHoursException extends DomainException {

    public MinimumWorkingHoursException() {
        super(MessageCode.ERROR_WORKING_HOURS_CANNOT_BE_LESS_THAN_ONE);
    }

}
