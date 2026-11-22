package br.com.ipet.ordering.domain.model.commons.exception;

import br.com.ipet.ordering.domain.model.DomainException;
import br.com.ipet.ordering.domain.model.MessageCode;
import lombok.Getter;

@Getter
public class DateMustBeAfterNow extends DomainException {

    private final String field;

    public DateMustBeAfterNow(String field) {
        super(MessageCode.ERROR_DATE_MUST_BE_LATTER_THAN_NOW);
        this.field = field;
    }

}
