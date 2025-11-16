package br.com.ipet.ordering.domain.exception;

import br.com.ipet.ordering.domain.exception.message.MessageCode;
import lombok.Getter;

@Getter
public class SchedulingItemNotFoundException extends DomainException {

    private final String[] fields;

    public SchedulingItemNotFoundException(String... fields) {
        super(MessageCode.ERROR_SCHEDULING_ITEM_NOT_FOUND);
        this.fields = fields;
    }
}
