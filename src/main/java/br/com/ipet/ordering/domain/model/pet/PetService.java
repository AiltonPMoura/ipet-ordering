package br.com.ipet.ordering.domain.model.pet;

import br.com.ipet.ordering.domain.model.FieldValidator;
import br.com.ipet.ordering.domain.model.customer.CustomerId;
import br.com.ipet.ordering.domain.model.customer.CustomerNotFoundException;
import br.com.ipet.ordering.domain.model.customer.Customers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PetService {
    private final Customers customers;

    public Pet register(CustomerId customerId, PetProfile petProfile) {
        FieldValidator.requiresNonNull("customerId", customerId);
        FieldValidator.requiresNonNull("petProfile", petProfile);

        this.verifyCustomerExists(customerId);

        return Pet.createNew()
                .customerId(customerId)
                .name(petProfile.name())
                .petType(petProfile.petType())
                .breed(petProfile.breed())
                .gender(petProfile.gender())
                .size(petProfile.size())
                .weight(petProfile.weight())
                .age(petProfile.age())
                .build();
    }

    public void change(Pet pet, CustomerId customerId, PetProfile petProfile) {
        FieldValidator.requiresNonNull("pet", pet);
        FieldValidator.requiresNonNull("customerId", customerId);
        FieldValidator.requiresNonNull("petProfile", petProfile);

        this.verifyIfBelongsToTheCustomer(customerId, pet);

        pet.changeName(petProfile.name());
        pet.changeType(petProfile.petType());
        pet.changeBreed(petProfile.breed());
        pet.changeGender(petProfile.gender());
        pet.changeSize(petProfile.size());
        pet.changeWeight(petProfile.weight());
        pet.changeAge(petProfile.age());
    }

    private void verifyIfBelongsToTheCustomer(CustomerId customerId, Pet pet) {
        if (!pet.customerId().equals(customerId))
            throw new PetDoesNotBelongToTheCustomer();
    }

    private void verifyCustomerExists(CustomerId customerId) {
        if (!customers.exists(customerId))
            throw new CustomerNotFoundException("");
    }

}
