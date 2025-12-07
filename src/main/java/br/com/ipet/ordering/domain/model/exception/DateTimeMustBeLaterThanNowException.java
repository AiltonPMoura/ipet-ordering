package br.com.ipet.ordering.domain.model.exception;

import br.com.ipet.ordering.domain.model.exception.message.MessageCode;
import lombok.Getter;

@Getter
public class DateTimeMustBeLaterThanNowException extends DomainException {

    private final String field;

    public DateTimeMustBeLaterThanNowException(String field) {
        super(MessageCode.ERROR_DATE_TIME_MUST_BE_LATTER_THAN_NOW);
        this.field = field;
    }

}
