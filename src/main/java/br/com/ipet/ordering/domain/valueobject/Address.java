package br.com.ipet.ordering.domain.valueobject;

import br.com.ipet.ordering.domain.util.FieldValidator;
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
        FieldValidator.notBlank("Street", street);
        FieldValidator.notNull("number", number);
        FieldValidator.notBlank("neighborhood", neighborhood);
        FieldValidator.notBlank("city", city);
        FieldValidator.notBlank("state", state);
        FieldValidator.notNull("zipCode", zipCode);

    }

}
