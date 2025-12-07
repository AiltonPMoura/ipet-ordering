package br.com.ipet.ordering.domain.model.exception;

import br.com.ipet.ordering.domain.model.exception.message.MessageCode;

public class QuantityGreaterThanZeroException extends DomainException {

    public QuantityGreaterThanZeroException() {
        super(MessageCode.ERROR_QUANTITY_GREATER_THAN_ZERO);
    }
}
