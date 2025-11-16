package br.com.ipet.ordering.domain.exception;

import br.com.ipet.ordering.domain.exception.message.MessageCode;

public class ServiceNameCannotBeVerySmallException extends DomainException {

    public ServiceNameCannotBeVerySmallException() {
        super(MessageCode.ERROR_SERVICE_NAME_CANNOT_BE_VERY_SMALL);
    }
}
