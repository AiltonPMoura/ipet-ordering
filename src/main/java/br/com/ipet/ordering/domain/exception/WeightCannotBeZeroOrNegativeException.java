package br.com.ipet.ordering.domain.exception;

import br.com.ipet.ordering.domain.exception.message.MessageCode;

public class WeightCannotBeZeroOrNegativeException extends DomainException {

    public WeightCannotBeZeroOrNegativeException() {
        super(MessageCode.ERROR_WEIGHT_CANNOT_BE_ZERO_OR_NEGATIVE);
    }

}
