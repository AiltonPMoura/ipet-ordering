package br.com.ipet.ordering.application.pet.management;

import br.com.ipet.ordering.application.util.Mapper;
import br.com.ipet.ordering.domain.model.FieldValidator;
import br.com.ipet.ordering.domain.model.customer.CustomerId;
import br.com.ipet.ordering.domain.model.pet.Breed;
import br.com.ipet.ordering.domain.model.pet.Gender;
import br.com.ipet.ordering.domain.model.pet.PetAge;
import br.com.ipet.ordering.domain.model.pet.PetId;
import br.com.ipet.ordering.domain.model.pet.PetName;
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
public class PetApplicationService {

    private final PetService petService;
    private final Pets pets;
    private final Mapper mapper;

    @Transactional
    public UUID create(PetInput input) {
        FieldValidator.requiresNonNull("pet input", input);

        var pet = petService.register(
                new CustomerId(input.getCustomerId()),
                new PetName(input.getName()),
                Type.valueOf(input.getType()),
                Breed.valueOf(input.getBreed()),
                Gender.valueOf(input.getGender()),
                Size.valueOf(input.getSize()),
                new PetWeight(input.getWeight()),
                new PetAge(input.getAge())
        );

        pets.add(pet);

        return pet.id().value();
    }

    @Transactional
    public void update(UUID petId, UUID customerId, PetUpdateInput input) {
        var pet = petService.change(new PetId(petId), new CustomerId(customerId),
                new PetName(input.getName()),
                Type.valueOf(input.getType()),
                Breed.valueOf(input.getBreed()),
                Gender.valueOf(input.getGender()),
                Size.valueOf(input.getSize()),
                new PetWeight(input.getWeight()),
                new PetAge(input.getAge()));

        pets.add(pet);
    }

}
