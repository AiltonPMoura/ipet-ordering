package br.com.ipet.ordering.infrastructure.persistence.repository;

import br.com.ipet.ordering.infrastructure.persistence.entity.AppointmentSchedulePersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface AppointmentSchedulePersistenceRepository
        extends JpaRepository<AppointmentSchedulePersistenceEntity, UUID>, JpaSpecificationExecutor<AppointmentSchedulePersistenceEntity> {

    boolean existsByCompanyId(UUID companyId);
}
