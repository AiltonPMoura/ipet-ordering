package br.com.ipet.ordering.domain.valueobject;

import br.com.ipet.ordering.domain.util.FieldValidator;

public record FullName(String firstName, String lastName) {

    public FullName {
        FieldValidator.requiresNonBlank("firstName", firstName);
        FieldValidator.requiresNonBlank("lastName", lastName);
    }

}
