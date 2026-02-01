package br.com.ipet.ordering.domain.model.exception;

import br.com.ipet.ordering.domain.model.DomainException;
import br.com.ipet.ordering.domain.model.MessageCode;

public class QuantityGreaterThanZeroException extends DomainException {

    public QuantityGreaterThanZeroException() {
        super(MessageCode.ERROR_QUANTITY_GREATER_THAN_ZERO);
    }
}
