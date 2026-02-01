package br.com.ipet.ordering.domain.model.exception;

import br.com.ipet.ordering.domain.model.DomainException;
import br.com.ipet.ordering.domain.model.MessageCode;
import lombok.Getter;

@Getter
public class EndDateOrTimeMustBeLaterThanStartException extends DomainException {

    private final String[] values;

    public EndDateOrTimeMustBeLaterThanStartException(String... values) {
        super(MessageCode.ERROR_START_TIME_CANNOT_BE_GREATER_THAN_OR_EQUALS_END_TIME);
        this.values = values;
    }

}
