package br.com.ipet.ordering.domain.exception;

import br.com.ipet.ordering.domain.exception.message.MessageCode;
import lombok.Getter;

@Getter
public class OrderIsNotDraftToChangeException extends DomainException {

    private final String orderId;

    public OrderIsNotDraftToChangeException(String orderId) {
        super(MessageCode.ERROR_ORDER_IS_NOT_DRAFT_TO_CHANE);
        this.orderId = orderId;
    }
}
