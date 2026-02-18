package br.com.ipet.ordering.infrastructure.persistence.pet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Set;
import java.util.UUID;

public interface PetPersistenceRepository extends JpaRepository<PetPersistenceEntity, UUID>, JpaSpecificationExecutor<PetPersistenceEntity> {
    Set<PetPersistenceEntity> findByCustomer_Id(UUID customerId);
}
