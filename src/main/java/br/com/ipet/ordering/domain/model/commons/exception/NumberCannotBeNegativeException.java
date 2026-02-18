package br.com.ipet.ordering.domain.model.commons.exception;

import br.com.ipet.ordering.domain.model.DomainException;
import br.com.ipet.ordering.domain.model.MessageCode;
import lombok.Getter;

@Getter
public class NumberCannotBeNegativeException extends DomainException {

    private final String field;

    public NumberCannotBeNegativeException(String field) {
        super(MessageCode.ERROR_NUMBER_CANNOT_BE_NEGATIVE);
        this.field = field;
    }
}
