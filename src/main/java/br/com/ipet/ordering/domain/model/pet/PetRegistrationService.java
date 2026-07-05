package br.com.ipet.ordering.domain.model.pet;

import br.com.ipet.ordering.domain.model.FieldValidator;
import br.com.ipet.ordering.domain.model.customer.CustomerId;
import br.com.ipet.ordering.domain.model.customer.CustomerNotFoundException;
import br.com.ipet.ordering.domain.model.customer.Customers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PetRegistrationService {
    private final Customers customers;

    public Pet register(CustomerId customerId, PetName name,
                        PetType petType, Breed breed, PetGender gender,
                        PetSize size, PetWeight weight, PetAge age) {
        FieldValidator.requiresNonNull("customerId", customerId);
        FieldValidator.requiresNonNull("name", name);
        FieldValidator.requiresNonNull("type", petType);
        FieldValidator.requiresNonNull("breed", breed);
        FieldValidator.requiresNonNull("gender", gender);
        FieldValidator.requiresNonNull("size", size);

        this.verifyCustomerExists(customerId);

        return Pet.createNew()
                .customerId(customerId)
                .name(name)
                .petType(petType)
                .breed(breed)
                .gender(gender)
                .size(size)
                .weight(weight)
                .age(age)
                .build();
    }

    private void verifyCustomerExists(CustomerId customerId) {
        if (!customers.exists(customerId))
            throw new CustomerNotFoundException("");
    }

}
