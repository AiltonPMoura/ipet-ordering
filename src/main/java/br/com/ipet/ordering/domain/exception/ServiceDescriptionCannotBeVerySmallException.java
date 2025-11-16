package br.com.ipet.ordering.domain.exception;

import br.com.ipet.ordering.domain.exception.message.MessageCode;

public class ServiceDescriptionCannotBeVerySmallException extends DomainException {

    public ServiceDescriptionCannotBeVerySmallException() {
        super(MessageCode.ERROR_SERVICE_DESCRIPTION_CANNOT_BE_VERY_SMALL);
    }
}
