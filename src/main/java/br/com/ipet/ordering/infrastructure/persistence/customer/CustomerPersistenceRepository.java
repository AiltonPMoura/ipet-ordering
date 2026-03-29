package br.com.ipet.ordering.infrastructure.persistence.customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface CustomerPersistenceRepository extends JpaRepository<CustomerPersistenceEntity, UUID>, JpaSpecificationExecutor<CustomerPersistenceEntity> {
    boolean existsByEmailAndIdNot(String email, UUID id);

}
