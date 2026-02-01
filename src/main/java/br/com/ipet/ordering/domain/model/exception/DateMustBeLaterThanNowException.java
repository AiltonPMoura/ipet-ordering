package br.com.ipet.ordering.domain.model.exception;

import br.com.ipet.ordering.domain.model.DomainException;
import br.com.ipet.ordering.domain.model.MessageCode;
import lombok.Getter;

@Getter
public class DateMustBeLaterThanNowException extends DomainException {

    private final String field;

    public DateMustBeLaterThanNowException(String field) {
        super(MessageCode.ERROR_DATE_MUST_BE_LATTER_THAN_NOW);
        this.field = field;
    }

}
