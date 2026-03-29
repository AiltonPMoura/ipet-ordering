package br.com.ipet.ordering.domain.model.order;

import br.com.ipet.ordering.domain.model.DomainException;
import br.com.ipet.ordering.domain.model.MessageCode;
import lombok.Getter;

@Getter
public class OrderDoesNotContainItemException extends DomainException {

    private final String[] fields;

    public OrderDoesNotContainItemException(String... fields) {
        super(MessageCode.ERROR_ORDER_DOES_NOT_CONTAIN_ITEM);
        this.fields = fields;
    }
}
