package br.com.ipet.ordering.domain.model.commons.exception;

import br.com.ipet.ordering.domain.model.DomainException;
import br.com.ipet.ordering.domain.model.MessageCode;
import lombok.Getter;

@Getter
public class CannotChangeStatusException extends DomainException {

    private final String[] status;

    public CannotChangeStatusException(String currentStatus, String newStatus) {
        super(MessageCode.ERROR_CANNOT_CHANGE_STATUS);
        this.status = new String[]{currentStatus, newStatus};
    }

}
