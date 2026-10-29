package br.com.ipet.ordering.infrastructure.persistence.transport;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface PetTransportPersistenceRepository
        extends JpaRepository<PetTransportPersistenceEntity, UUID>,
        JpaSpecificationExecutor<PetTransportPersistenceEntity> {

    Optional<PetTransportPersistenceEntity> findByIdAndCompany_Id(UUID id, UUID companyId);

    long countByCompany_Id(UUID companyId);

    boolean existsByCompany_Id(UUID companyId);

    @Query("""
            SELECT CASE WHEN COUNT(transport) > 0 THEN true ELSE false END
            FROM PetTransportPersistenceEntity transport
            WHERE transport.company.id = :companyId
              AND transport.status = 'ACTIVE'
            """)
    boolean existsActiveByCompanyId(@Param("companyId") UUID companyId);

    boolean existsByVehicle_LicensePlateAndIdNot(String licensePlate, UUID id);

    @Query(value = """
            SELECT *
            FROM pet_transport
            WHERE company_id = :companyId
              AND status = 'ACTIVE'
            ORDER BY pet_capacity DESC
            FETCH FIRST 1 ROW ONLY
    """, nativeQuery = true)
    Optional<PetTransportPersistenceEntity> findHighestCapacityActiveByCompanyId(UUID companyId);

}
