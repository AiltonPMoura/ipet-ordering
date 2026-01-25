package br.com.ipet.ordering.infrastructure.persistence.company;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CompanyPersistenceRepository extends JpaRepository<CompanyPersistence, UUID> {
    boolean existsByEmailAndIdNot(String email, UUID id);
    boolean existsByCnpjAndIdNot(String cnpj, UUID id);
}
