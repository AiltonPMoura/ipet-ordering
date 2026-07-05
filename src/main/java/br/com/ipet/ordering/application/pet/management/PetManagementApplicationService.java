package br.com.ipet.ordering.application.pet.management;

import br.com.ipet.ordering.domain.model.FieldValidator;
import br.com.ipet.ordering.domain.model.customer.CustomerId;
import br.com.ipet.ordering.domain.model.pet.Breed;
import br.com.ipet.ordering.domain.model.pet.Pet;
import br.com.ipet.ordering.domain.model.pet.PetAge;
import br.com.ipet.ordering.domain.model.pet.PetGender;
import br.com.ipet.ordering.domain.model.pet.PetId;
import br.com.ipet.ordering.domain.model.pet.PetName;
import br.com.ipet.ordering.domain.model.pet.PetNotFoundException;
import br.com.ipet.ordering.domain.model.pet.PetRegistrationService;
import br.com.ipet.ordering.domain.model.pet.PetSize;
import br.com.ipet.ordering.domain.model.pet.PetType;
import br.com.ipet.ordering.domain.model.pet.PetWeight;
import br.com.ipet.ordering.domain.model.pet.Pets;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class PetManagementApplicationService {
    private final PetRegistrationService petRegistrationService;
    private final Pets pets;

    public UUID create(UUID customerId, PetInput input) {
        FieldValidator.requiresNonNull("pet input", input);

        var pet = petRegistrationService.register(
                new CustomerId(customerId),
                new PetName(input.getName()),
                PetType.valueOf(input.getType()),
                Breed.valueOf(input.getBreed()),
                PetGender.valueOf(input.getGender()),
                PetSize.valueOf(input.getSize()),
                new PetWeight(input.getWeight()),
                new PetAge(input.getAge())
        );

        pets.add(pet);

        return pet.id().value();
    }

    public void update(UUID petId, UUID rawCustomerId, PetUpdateInput input) {
        FieldValidator.requiresNonNull("petId", petId);
        FieldValidator.requiresNonNull("customerId", rawCustomerId);
        FieldValidator.requiresNonNull("petUpdateInput", input);

        var pet = this.findPet(petId);
        var customerId = new CustomerId(rawCustomerId);

        pet.changeName(new PetName(input.getName()), customerId);
        pet.changeType(PetType.valueOf(input.getType()), customerId);
        pet.changeBreed(Breed.valueOf(input.getBreed()), customerId);
        pet.changeGender(PetGender.valueOf(input.getGender()), customerId);
        pet.changeSize(PetSize.valueOf(input.getSize()), customerId);
        pet.changeWeight(new PetWeight(input.getWeight()), customerId);
        pet.changeAge(new PetAge(input.getAge()), customerId);

        pets.add(pet);
    }

    public void delete(UUID petId, UUID rawCustomerId) {
        FieldValidator.requiresNonNull("petId", petId);
        FieldValidator.requiresNonNull("customerId", rawCustomerId);

        var pet = this.findPet(petId);
        pet.discard(new CustomerId(rawCustomerId));

        pets.remove(pet);
    }

    private Pet findPet(UUID petId) {
        return pets.ofId(new PetId(petId)).orElseThrow(() -> new PetNotFoundException(""));
    }

}
