package br.com.ipet.ordering.infrastructure.persistence.repository;

import br.com.ipet.ordering.infrastructure.persistence.entity.DayTimeSchedulePersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface DayTimeSchedulePersistenceRepository
        extends JpaRepository<DayTimeSchedulePersistenceEntity, UUID>, JpaSpecificationExecutor<DayTimeSchedulePersistenceEntity> {

    boolean existsByCompanyId(UUID companyId);
}
