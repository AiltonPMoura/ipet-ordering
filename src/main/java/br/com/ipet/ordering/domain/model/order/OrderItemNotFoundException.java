package br.com.ipet.ordering.domain.model.order;

import br.com.ipet.ordering.domain.model.DomainException;
import br.com.ipet.ordering.domain.model.MessageCode;
import lombok.Getter;

@Getter
public class OrderItemNotFoundException extends DomainException {

    private final String[] fields;

    public OrderItemNotFoundException(String... fields) {
        super(MessageCode.ERROR_ORDER_ITEM_NOT_FOUND);
        this.fields = fields;
    }
}
