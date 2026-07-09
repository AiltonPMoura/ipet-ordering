package br.com.ipet.ordering.domain.model.commons.exception;

import br.com.ipet.ordering.domain.model.DomainException;
import br.com.ipet.ordering.domain.model.MessageCode;
import lombok.Getter;

@Getter
public class FieldCannotBeEmptyException extends DomainException {

    public FieldCannotBeEmptyException(String field) {
        super(MessageCode.ERROR_FIELD_CANNOT_BE_EMPTY, field);
    }

}
