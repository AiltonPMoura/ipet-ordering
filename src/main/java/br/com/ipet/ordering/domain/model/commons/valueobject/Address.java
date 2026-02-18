package br.com.ipet.ordering.domain.model.commons.valueobject;

import br.com.ipet.ordering.domain.model.FieldValidator;
import lombok.Builder;

@Builder
public record Address(String street,
                      Integer number,
                      String neighborhood,
                      String complement,
                      String city,
                      String state,
                      ZipCode zipCode) {

    public Address {
        FieldValidator.requiresNonBlank("Street", street);
        FieldValidator.requiresNonNull("number", number);
        FieldValidator.requiresNonBlank("neighborhood", neighborhood);
        FieldValidator.requiresNonBlank("city", city);
        FieldValidator.requiresNonBlank("state", state);
        FieldValidator.requiresNonNull("zipCode", zipCode);

    }

}
