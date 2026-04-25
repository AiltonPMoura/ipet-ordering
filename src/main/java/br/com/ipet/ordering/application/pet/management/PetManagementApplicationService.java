package br.com.ipet.ordering.application.pet.management;

import br.com.ipet.ordering.domain.model.FieldValidator;
import br.com.ipet.ordering.domain.model.customer.CustomerId;
import br.com.ipet.ordering.domain.model.pet.Breed;
import br.com.ipet.ordering.domain.model.pet.Gender;
import br.com.ipet.ordering.domain.model.pet.PetAge;
import br.com.ipet.ordering.domain.model.pet.PetId;
import br.com.ipet.ordering.domain.model.pet.PetName;
import br.com.ipet.ordering.domain.model.pet.PetNotFoundException;
import br.com.ipet.ordering.domain.model.pet.PetProfile;
import br.com.ipet.ordering.domain.model.pet.PetService;
import br.com.ipet.ordering.domain.model.pet.PetWeight;
import br.com.ipet.ordering.domain.model.pet.Pets;
import br.com.ipet.ordering.domain.model.pet.Size;
import br.com.ipet.ordering.domain.model.pet.Type;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class PetManagementApplicationService {
    private final PetService petService;
    private final Pets pets;

    public UUID create(PetInput input) {
        FieldValidator.requiresNonNull("pet input", input);

        var pet = petService.register(
                new CustomerId(input.getCustomerId()),
                PetProfile.builder()
                        .name(new PetName(input.getName()))
                        .type(Type.valueOf(input.getType()))
                        .breed(Breed.valueOf(input.getBreed()))
                        .gender(Gender.valueOf(input.getGender()))
                        .size(Size.valueOf(input.getSize()))
                        .weight(new PetWeight(input.getWeight()))
                        .age(new PetAge(input.getAge()))
                        .build()
        );

        pets.add(pet);

        return pet.id().value();
    }

    public void update(UUID petId, UUID customerId, PetUpdateInput input) {
        FieldValidator.requiresNonNull("petId", petId);
        FieldValidator.requiresNonNull("customerId", customerId);
        FieldValidator.requiresNonNull("petUpdateInput", input);

        var pet = pets.ofId(new PetId(petId)).orElseThrow(PetNotFoundException::new);

        petService.change(
                pet,
                new CustomerId(customerId),
                PetProfile.builder()
                        .name(new PetName(input.getName()))
                        .type(Type.valueOf(input.getType()))
                        .breed(Breed.valueOf(input.getBreed()))
                        .gender(Gender.valueOf(input.getGender()))
                        .size(Size.valueOf(input.getSize()))
                        .weight(new PetWeight(input.getWeight()))
                        .age(new PetAge(input.getAge()))
                        .build()
        );

        pets.add(pet);
    }

}
