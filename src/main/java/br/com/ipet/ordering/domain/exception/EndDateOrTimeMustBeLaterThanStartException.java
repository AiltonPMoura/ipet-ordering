package br.com.ipet.ordering.domain.exception;

import br.com.ipet.ordering.domain.exception.message.MessageCode;
import lombok.Getter;

@Getter
public class EndDateOrTimeMustBeLaterThanStartException extends DomainException {

    private final String[] fields;

    public EndDateOrTimeMustBeLaterThanStartException(String... fields) {
        super(MessageCode.ERROR_START_TIME_CANNOT_BE_GREATER_THAN_OR_EQUALS_END_TIME);
        this.fields = fields;
    }

}
