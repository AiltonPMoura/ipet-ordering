package br.com.ipet.ordering.domain.model.commons;

import br.com.ipet.ordering.domain.model.exception.ServiceDescriptionCannotBeVerySmallException;
import br.com.ipet.ordering.domain.model.util.FieldValidator;

public record ServiceDescription(String value) {

    public ServiceDescription {
        FieldValidator.requiresNonBlank("service description", value);
        if (value.length() < 4)
            throw new ServiceDescriptionCannotBeVerySmallException();
    }

}
