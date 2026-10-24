package br.com.ipet.ordering.infrastructure.persistence.transport;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface PetTransportPersistenceRepository extends
        JpaRepository<PetTransportPersistenceEntity, UUID>,
        JpaSpecificationExecutor<PetTransportPersistenceEntity> {
    Optional<PetTransportPersistenceEntity> findByIdAndCompany_Id(UUID id, UUID companyId);
        Optional<PetTransportPersistenceEntity> findFirstByCompany_Id(UUID companyId);

        long countByCompany_Id(UUID companyId);
}
