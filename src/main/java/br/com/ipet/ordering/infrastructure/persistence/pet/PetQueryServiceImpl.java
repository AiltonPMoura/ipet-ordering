package br.com.ipet.ordering.infrastructure.persistence.pet;

import br.com.ipet.ordering.application.pet.query.PetDetailOutput;
import br.com.ipet.ordering.application.pet.query.PetFilter;
import br.com.ipet.ordering.application.pet.query.PetQueryService;
import br.com.ipet.ordering.application.pet.query.PetSummaryOutput;
import br.com.ipet.ordering.application.util.Mapper;
import br.com.ipet.ordering.domain.model.pet.PetNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static br.com.ipet.ordering.infrastructure.persistence.pet.PetPersistenceSpecification.*;

@Component
@RequiredArgsConstructor
public class PetQueryServiceImpl implements PetQueryService {

    private final PetPersistenceRepository repository;
    private final Mapper mapper;

    @Override
    public PetDetailOutput findById(UUID petId) {
        var pet = repository.findById(petId)
                .orElseThrow(PetNotFoundException::new);

        return mapper.convert(pet, PetDetailOutput.class);
    }

    @Override
    public Page<PetSummaryOutput> filter(PetFilter petFilter, Pageable pageable) {
        return repository.findAll(toSpecification(petFilter), pageable)
                .map(petPersistence ->
                        mapper.convert(petPersistence, PetSummaryOutput.class));
    }

    private static Specification<PetPersistenceEntity> toSpecification(PetFilter petFilter) {
        return customerId(petFilter.getCustomerId())
                .and(name(petFilter.getName())
                        .or(type(petFilter.getType()))
                        .or(size(petFilter.getSize()))
                        .or(breed(petFilter.getBreed()))
                );
    }


}
