package br.com.ipet.ordering.domain.model.valueobject;

import br.com.ipet.ordering.domain.model.util.FieldValidator;
import lombok.Builder;

@Builder
public record FullName(String firstName, String lastName) {

    public FullName {
        FieldValidator.requiresNonBlank("firstName", firstName);
        FieldValidator.requiresNonBlank("lastName", lastName);
    }

}
