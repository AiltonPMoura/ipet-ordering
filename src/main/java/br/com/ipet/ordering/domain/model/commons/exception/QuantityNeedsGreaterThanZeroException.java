package br.com.ipet.ordering.domain.model.commons.exception;


import br.com.ipet.ordering.domain.model.DomainException;
import br.com.ipet.ordering.domain.model.MessageCode;

public class QuantityNeedsGreaterThanZeroException extends DomainException {

    public QuantityNeedsGreaterThanZeroException() {
        super(MessageCode.ERROR_QUANTITY_NEEDS_GREATER_THAN_ZERO);
    }
}
