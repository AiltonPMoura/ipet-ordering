package br.com.ipet.ordering.domain.model.exception;

import br.com.ipet.ordering.domain.model.DomainException;
import br.com.ipet.ordering.domain.model.MessageCode;
import lombok.Getter;

@Getter
public class CannotBeChangeStatusException extends DomainException {

    private final String[] status;

    public CannotBeChangeStatusException(String currentStatus, String newStatus) {
        super(MessageCode.ERROR_CANNOT_BE_CHANGE_STATUS);
        this.status = new String[]{currentStatus, newStatus};
    }

}
