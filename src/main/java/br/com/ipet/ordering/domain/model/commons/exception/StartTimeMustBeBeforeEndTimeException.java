package br.com.ipet.ordering.domain.model.commons.exception;

import br.com.ipet.ordering.domain.model.DomainException;
import br.com.ipet.ordering.domain.model.MessageCode;
import lombok.Getter;

@Getter
public class StartTimeMustBeBeforeEndTimeException extends DomainException {

    private final String[] values;

    public StartTimeMustBeBeforeEndTimeException(String... values) {
        super(MessageCode.ERROR_START_TIME_CANNOT_BE_GREATER_THAN_END_TIME);
        this.values = values;
    }

}
