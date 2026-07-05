package br.com.ipet.ordering.infrastructure.persistence.pet;

import br.com.ipet.ordering.domain.model.customer.CustomerId;
import br.com.ipet.ordering.domain.model.pet.Pet;
import br.com.ipet.ordering.domain.model.pet.PetId;
import br.com.ipet.ordering.domain.model.pet.Pets;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PetPersistenceProvider implements Pets {

    private final PetPersistenceRepository petRepository;
    private final PetMapper petMapper;
    private final PetPersistenceMapper petPersistenceMapper;

    @Override
    @Transactional(readOnly = true)
    public Optional<Pet> ofId(PetId id) {
        return petRepository.findById(id.value()).map(petMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean exists(PetId id) {
        return petRepository.existsById(id.value());
    }

    @Override
    @Transactional
    public void add(Pet pet) {
        petRepository.findById(pet.id().value())
                .ifPresentOrElse(
                        petPersistence -> this.update(petPersistence, pet),
                        () -> this.insert(pet));
    }

    private void insert(Pet pet) {
        var petPersistence = petPersistenceMapper.fromDomain(pet);
        petRepository.saveAndFlush(petPersistence);
    }

    private void update(PetPersistenceEntity petPersistence, Pet pet) {
        petPersistence = petPersistenceMapper.merge(petPersistence, pet);
        petRepository.saveAndFlush(petPersistence);
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return petRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Set<Pet> ofCustomer(CustomerId customerId) {
        return petRepository.findByCustomer_Id(customerId.value())
                .stream()
                .map(petMapper::toDomain)
                .collect(Collectors.toSet());
    }

    @Override
    public void remove(Pet pet) {
        petRepository.deleteById(pet.id().value());
    }

    @Override
    public void remove(PetId id) {
        petRepository.deleteById(id.value());
    }
}
