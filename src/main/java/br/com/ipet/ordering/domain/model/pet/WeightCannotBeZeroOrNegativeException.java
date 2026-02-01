package br.com.ipet.ordering.domain.model.pet;

import br.com.ipet.ordering.domain.model.DomainException;
import br.com.ipet.ordering.domain.model.MessageCode;

public class WeightCannotBeZeroOrNegativeException extends DomainException {

    public WeightCannotBeZeroOrNegativeException() {
        super(MessageCode.ERROR_WEIGHT_CANNOT_BE_ZERO_OR_NEGATIVE);
    }

}
