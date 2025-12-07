package br.com.ipet.ordering.domain.model.exception;

import br.com.ipet.ordering.domain.model.exception.message.MessageCode;
import lombok.Getter;

@Getter
public class ServiceNotFoundException extends DomainException {

    private final String[] fields;

    public ServiceNotFoundException(String... fields) {
        super(MessageCode.ERROR_SERVICE_NOT_FOUND);
        this.fields = fields;
    }

}
