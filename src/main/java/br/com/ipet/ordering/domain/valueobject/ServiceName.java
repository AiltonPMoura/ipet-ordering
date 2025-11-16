package br.com.ipet.ordering.domain.valueobject;

import br.com.ipet.ordering.domain.exception.ServiceNameCannotBeVerySmallException;
import br.com.ipet.ordering.domain.util.FieldValidator;

public record ServiceName(String value) {

    public ServiceName {
        FieldValidator.requiresNonBlank("service name", value);
        if (value.length() < 4)
            throw new ServiceNameCannotBeVerySmallException();
    }

}
