package br.com.ipet.ordering.domain.exception;

import br.com.ipet.ordering.domain.exception.message.MessageCode;
import lombok.Getter;

@Getter
public class OrderIsNotDraftToChangeException extends DomainException {

    private final String orderId;

    public OrderIsNotDraftToChangeException(String orderId) {
        super(MessageCode.ERROR_FIELD_CANNOT_BE_EMPTY);
        this.orderId = orderId;
    }
}
