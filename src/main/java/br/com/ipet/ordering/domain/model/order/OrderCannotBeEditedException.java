package br.com.ipet.ordering.domain.model.order;

import br.com.ipet.ordering.domain.model.DomainException;
import br.com.ipet.ordering.domain.model.MessageCode;
import lombok.Getter;

@Getter
public class OrderCannotBeEditedException extends DomainException {

    private final String[] fields;

    public OrderCannotBeEditedException(String... fields) {
        super(MessageCode.ERROR_ORDER_CANNOT_BE_EDITED);
        this.fields = fields;
    }
}
