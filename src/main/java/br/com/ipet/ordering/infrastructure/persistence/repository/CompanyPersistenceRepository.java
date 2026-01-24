package br.com.ipet.ordering.infrastructure.persistence.repository;

import br.com.ipet.ordering.infrastructure.persistence.entity.CompanyPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CompanyPersistenceRepository extends JpaRepository<CompanyPersistenceEntity, UUID> {
    boolean existsByEmailAndIdNot(String email, UUID id);
}
