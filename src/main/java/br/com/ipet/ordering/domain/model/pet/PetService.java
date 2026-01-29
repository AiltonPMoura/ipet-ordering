package br.com.ipet.ordering.domain.model.pet;

import br.com.ipet.ordering.domain.model.commons.CustomerId;
import br.com.ipet.ordering.domain.model.commons.PetId;
import br.com.ipet.ordering.domain.model.commons.PetName;
import br.com.ipet.ordering.domain.model.commons.Weight;
import br.com.ipet.ordering.domain.model.customer.CustomerNotFoundException;
import br.com.ipet.ordering.domain.model.customer.Customers;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PetService {

    private final Customers customers;
    private final Pets pets;

    public Pet register(PetName name, CustomerId customerId,
                         Type type, Breed breed, Gender gender,
                         Size size, Weight weight) {

        verifyCustomerExists(customerId);

        return Pet.createNew()
                .customerId(customerId)
                .name(name)
                .type(type)
                .breed(breed)
                .gender(gender)
                .size(size)
                .weight(weight)
                .build();
    }

    public Pet change(PetId id, CustomerId customerId,
                      PetName name, Type type, Breed breed,
                      Gender gender, Size size, Weight weight) {

        verifyCustomerExists(customerId);

        var pet = pets.ofCustomer(id, customerId)
                .orElseThrow(PetDoesNotBelongToTheCustomer::new);

        pet.changeName(name);
        pet.changeType(type);
        pet.changeBreed(breed);
        pet.changeGender(gender);
        pet.changeSize(size);
        pet.changeWeight(weight);

        return pet;
    }

    private void verifyCustomerExists(CustomerId customerId) {
        if (!customers.exists(customerId))
            throw new CustomerNotFoundException();
    }

}
