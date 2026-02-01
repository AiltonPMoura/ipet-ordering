package br.com.ipet.ordering.domain.model.order;

import br.com.ipet.ordering.domain.model.DomainException;
import br.com.ipet.ordering.domain.model.MessageCode;
import lombok.Getter;

@Getter
public class OrderIsNotDraftToChangeException extends DomainException {

    private final String orderId;

    public OrderIsNotDraftToChangeException(String orderId) {
        super(MessageCode.ERROR_ORDER_IS_NOT_DRAFT_TO_CHANGE);
        this.orderId = orderId;
    }
}
