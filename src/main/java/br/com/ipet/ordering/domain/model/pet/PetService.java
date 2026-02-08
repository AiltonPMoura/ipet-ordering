package br.com.ipet.ordering.domain.model.pet;

import br.com.ipet.ordering.domain.model.customer.CustomerId;
import br.com.ipet.ordering.domain.model.customer.CustomerNotFoundException;
import br.com.ipet.ordering.domain.model.customer.Customers;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PetService {

    private final Customers customers;
    private final Pets pets;

    public Pet register(CustomerId customerId, PetName name,
                         Type type, Breed breed, Gender gender,
                         Size size, PetWeight weight, PetAge age) {

        verifyCustomerExists(customerId);

        return Pet.createNew()
                .customerId(customerId)
                .name(name)
                .type(type)
                .breed(breed)
                .gender(gender)
                .size(size)
                .weight(weight)
                .age(age)
                .build();
    }

    public Pet change(PetId petId, CustomerId customerId,
                      PetName name, Type type, Breed breed, Gender gender,
                      Size size, PetWeight weight, PetAge age) {

        var pet = pets.ofId(petId).orElseThrow(PetNotFoundException::new);

        verifyIfBelongsToTheCustomer(customerId, pet);

        pet.changeName(name);
        pet.changeType(type);
        pet.changeBreed(breed);
        pet.changeGender(gender);
        pet.changeSize(size);
        pet.changeWeight(weight);
        pet.changeAge(age);

        return pet;
    }

    private void verifyIfBelongsToTheCustomer(CustomerId customerId, Pet pet) {
        verifyCustomerExists(customerId);

        var doesNottBelongsToTheCustomer = pets.ofCustomer(customerId)
                .stream()
                .noneMatch(pet::equals);

        if (doesNottBelongsToTheCustomer)
            throw new PetDoesNotBelongToTheCustomer();
    }

    private void verifyCustomerExists(CustomerId customerId) {
        if (!customers.exists(customerId))
            throw new CustomerNotFoundException();
    }

}
