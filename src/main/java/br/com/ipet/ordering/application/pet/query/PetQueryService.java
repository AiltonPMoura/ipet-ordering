package br.com.ipet.ordering.application.pet.query;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface PetQueryService {
    PetDetailOutput findById(UUID petId);
    Page<PetSummaryOutput> filter(PetFilter petFilter, Pageable pageable);
}
