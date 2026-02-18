package br.com.ipet.ordering.domain.model.commons.valueobject;

import br.com.ipet.ordering.domain.model.FieldValidator;
import br.com.ipet.ordering.domain.model.commons.exception.ServiceNameCannotBeVerySmallException;

public record ServiceName(String value) {

    public ServiceName {
        FieldValidator.requiresNonBlank("service name", value);
        if (value.length() < 4)
            throw new ServiceNameCannotBeVerySmallException();
    }

}
