package br.com.ipet.ordering.domain.valueobject;

import br.com.ipet.ordering.domain.exception.ServiceDescriptionCannotBeVerySmallException;
import br.com.ipet.ordering.domain.util.FieldValidator;

public record ServiceDescription(String value) {

    public ServiceDescription {
        FieldValidator.requiresNonBlank("service description", value);
        if (value.length() < 4)
            throw new ServiceDescriptionCannotBeVerySmallException();
    }

}
