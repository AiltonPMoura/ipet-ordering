package br.com.ipet.ordering.domain.model.commons.exception;

import br.com.ipet.ordering.domain.model.DomainException;
import br.com.ipet.ordering.domain.model.MessageCode;

public class TimeMustHaveMinutePrecisionException extends DomainException {

    public TimeMustHaveMinutePrecisionException(String field) {
        super(MessageCode.ERROR_TIME_MUST_HAVE_MINUTE_PRECISION, field);
    }
}
