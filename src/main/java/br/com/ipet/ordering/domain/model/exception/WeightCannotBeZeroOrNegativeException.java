package br.com.ipet.ordering.domain.model.exception;

import br.com.ipet.ordering.domain.model.exception.message.MessageCode;

public class WeightCannotBeZeroOrNegativeException extends DomainException {

    public WeightCannotBeZeroOrNegativeException() {
        super(MessageCode.ERROR_WEIGHT_CANNOT_BE_ZERO_OR_NEGATIVE);
    }

}
