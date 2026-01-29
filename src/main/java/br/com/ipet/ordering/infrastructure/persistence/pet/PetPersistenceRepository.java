package br.com.ipet.ordering.infrastructure.persistence.pet;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PetPersistenceRepository extends JpaRepository<PetPersistenceEntity, UUID> {
    Optional<PetPersistenceEntity> findByIdAndCustomerId(UUID id, UUID customerId);
}
