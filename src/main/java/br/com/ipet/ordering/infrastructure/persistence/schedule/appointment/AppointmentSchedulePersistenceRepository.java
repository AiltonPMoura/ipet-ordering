package br.com.ipet.ordering.infrastructure.persistence.schedule.appointment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface AppointmentSchedulePersistenceRepository
        extends JpaRepository<AppointmentSchedulePersistenceEntity, UUID>,
        JpaSpecificationExecutor<AppointmentSchedulePersistenceEntity> {

    boolean existsByCompany_Id(UUID companyId);
    Optional<AppointmentSchedulePersistenceEntity> findByCompany_Id(UUID companyId);

    @Query("""
            SELECT schedule
            FROM AppointmentSchedulePersistenceEntity schedule
            WHERE schedule.id = :id
              AND schedule.status = 'ACTIVE'
            """)
    Optional<AppointmentSchedulePersistenceEntity> findByIdAndActive(UUID id);

    @Query("""
            SELECT schedule
            FROM AppointmentSchedulePersistenceEntity schedule
            WHERE schedule.company.id = :companyId
              AND schedule.status = 'ACTIVE'
            """)
    Optional<AppointmentSchedulePersistenceEntity> findByCompanyIdAndActive(UUID companyId);
}
