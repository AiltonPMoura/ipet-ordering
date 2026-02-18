package br.com.ipet.ordering.domain.model.agenda;

import br.com.ipet.ordering.domain.model.DomainException;
import br.com.ipet.ordering.domain.model.MessageCode;

public class WorkingHoursCanNotBeLessThanOneException extends DomainException {

    public WorkingHoursCanNotBeLessThanOneException() {
        super(MessageCode.ERROR_SERVICE_NAME_CANNOT_BE_VERY_SMALL);
    }

}
