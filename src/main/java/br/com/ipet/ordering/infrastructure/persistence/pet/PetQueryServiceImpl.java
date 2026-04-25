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
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static br.com.ipet.ordering.infrastructure.persistence.pet.PetPersistenceSpecification.*;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PetQueryServiceImpl implements PetQueryService {

    private final PetPersistenceRepository petRepository;
    private final Mapper mapper;

    @Override
    public PetDetailOutput findById(UUID petId) {
        var petPersistence = petRepository.findById(petId).orElseThrow(() -> new PetNotFoundException(""));
        return mapper.convert(petPersistence, PetDetailOutput.class);
    }

    @Override
    public Page<PetSummaryOutput> filter(PetFilter petFilter, Pageable pageable) {
        return petRepository.findAll(this.toSpecification(petFilter), pageable)
                .map(petPersistence -> mapper.convert(petPersistence, PetSummaryOutput.class));
    }

    private Specification<PetPersistenceEntity> toSpecification(PetFilter petFilter) {
        return customerId(petFilter.getCustomerId())
                .and(name(petFilter.getName())
                        .or(type(petFilter.getType()))
                        .or(size(petFilter.getSize()))
                        .or(breed(petFilter.getBreed()))
                );
    }

}
