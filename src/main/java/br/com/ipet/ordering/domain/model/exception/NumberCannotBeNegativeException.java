package br.com.ipet.ordering.domain.model.exception;

import br.com.ipet.ordering.domain.model.exception.message.MessageCode;
import lombok.Getter;

@Getter
public class NumberCannotBeNegativeException extends DomainException {

    private final String field;

    public NumberCannotBeNegativeException(String field) {
        super(MessageCode.ERROR_NUMBER_CANNOT_BE_NEGATIVE);
        this.field = field;
    }
}
