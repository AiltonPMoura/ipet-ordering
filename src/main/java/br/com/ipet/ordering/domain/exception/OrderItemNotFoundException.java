package br.com.ipet.ordering.domain.exception;

import br.com.ipet.ordering.domain.exception.message.MessageCode;
import lombok.Getter;

@Getter
public class OrderItemNotFoundException extends DomainException {

    private final String[] fields;

    public OrderItemNotFoundException(String... fields) {
        super(MessageCode.ERROR_ORDER_ITEM_NOT_FOUND);
        this.fields = fields;
    }
}
