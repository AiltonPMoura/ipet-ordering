package br.com.ipet.ordering.domain.model.exception;

import br.com.ipet.ordering.domain.model.exception.message.MessageCode;
import lombok.Getter;

@Getter
public class OrderIsNotDraftToChangeException extends DomainException {

    private final String orderId;

    public OrderIsNotDraftToChangeException(String orderId) {
        super(MessageCode.ERROR_ORDER_IS_NOT_DRAFT_TO_CHANGE);
        this.orderId = orderId;
    }
}
