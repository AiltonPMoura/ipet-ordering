package br.com.ipet.ordering.infrastructure.persistence.schedule.appointment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface AppointmentSchedulePersistenceRepository
        extends JpaRepository<AppointmentSchedulePersistenceEntity, UUID>,
        JpaSpecificationExecutor<AppointmentSchedulePersistenceEntity> {

    boolean existsByCompanyId(UUID companyId);

    Optional<AppointmentSchedulePersistenceEntity> findByCompanyId(UUID companyId);

    Optional<AppointmentSchedulePersistenceEntity> findByIdAndStatus(UUID id, String status);

    Optional<AppointmentSchedulePersistenceEntity> findByCompanyIdAndStatus(UUID companyId, String status);
}
