package br.com.ipet.ordering.domain.valueobject;

import br.com.ipet.ordering.domain.util.FieldValidator;

public record FullName(String firstName, String lastName) {

    public FullName {
        FieldValidator.notBlank("firstName", firstName);
        FieldValidator.notBlank("lastName", lastName);
    }

}
