package br.com.ipet.ordering.infrastructure.persistence.pet;

import br.com.ipet.ordering.domain.model.customer.CustomerId;
import br.com.ipet.ordering.domain.model.pet.PetId;
import br.com.ipet.ordering.domain.model.pet.Pet;
import br.com.ipet.ordering.domain.model.pet.Pets;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PetPersistenceProvider implements Pets {

    private final PetPersistenceRepository persistenceRepository;
    private final PetMapper petMapper;

    @Override
    public Optional<Pet> ofId(PetId id) {
        return Optional.empty();
    }

    @Override
    public boolean exists(PetId id) {
        return false;
    }

    @Override
    public void add(Pet pet) {
    }

    @Override
    public int count() {
        return 0;
    }

    @Override
    public Set<Pet> ofCustomer(CustomerId customerId) {
        return persistenceRepository.findByCustomer_Id(customerId.value())
                .stream()
                .map(petMapper::toDomain)
                .collect(Collectors.toSet());
    }
}
