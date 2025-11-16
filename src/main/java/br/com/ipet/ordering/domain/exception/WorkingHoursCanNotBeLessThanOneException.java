package br.com.ipet.ordering.domain.exception;

import br.com.ipet.ordering.domain.exception.message.MessageCode;

public class WorkingHoursCanNotBeLessThanOneException extends DomainException {

    public WorkingHoursCanNotBeLessThanOneException() {
        super(MessageCode.ERROR_SERVICE_NAME_CANNOT_BE_VERY_SMALL);
    }

}
