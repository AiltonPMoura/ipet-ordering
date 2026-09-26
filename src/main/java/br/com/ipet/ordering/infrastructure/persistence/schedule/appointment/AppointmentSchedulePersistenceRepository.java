package br.com.ipet.ordering.infrastructure.persistence.schedule.appointment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface AppointmentSchedulePersistenceRepository
        extends JpaRepository<AppointmentSchedulePersistenceEntity, UUID>,
        JpaSpecificationExecutor<AppointmentSchedulePersistenceEntity> {

    boolean existsByCompany_Id(UUID companyId);
}
