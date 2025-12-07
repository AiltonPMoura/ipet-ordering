package br.com.ipet.ordering.domain.model.exception;

import br.com.ipet.ordering.domain.model.exception.message.MessageCode;
import lombok.Getter;

@Getter
public class CanNotChangeSchedulingAtException extends DomainException {

    private final String value;

    public CanNotChangeSchedulingAtException(String value) {
        super(MessageCode.ERROR_CANNOT_CHANGE_SCHEDULING_AT);
        this.value = value;
    }

}
