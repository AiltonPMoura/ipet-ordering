package br.com.ipet.ordering.infrastructure.persistence.company;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface CompanyPersistenceRepository extends JpaRepository<CompanyPersistenceEntity, UUID>, JpaSpecificationExecutor<CompanyPersistenceEntity> {
    boolean existsByEmailAndIdNot(String email, UUID id);
    boolean existsByCnpjAndIdNot(String cnpj, UUID id);
}
