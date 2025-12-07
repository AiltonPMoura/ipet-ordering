package br.com.ipet.ordering.domain.model.exception;

import br.com.ipet.ordering.domain.model.exception.message.MessageCode;
import lombok.Getter;

@Getter
public class OrderItemNotFoundException extends DomainException {

    private final String[] fields;

    public OrderItemNotFoundException(String... fields) {
        super(MessageCode.ERROR_ORDER_ITEM_NOT_FOUND);
        this.fields = fields;
    }
}
