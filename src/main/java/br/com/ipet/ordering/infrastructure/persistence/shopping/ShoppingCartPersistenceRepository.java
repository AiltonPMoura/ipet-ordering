package br.com.ipet.ordering.infrastructure.persistence.shopping;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ShoppingCartPersistenceRepository extends JpaRepository<ShoppingCartPersistenceEntity, UUID> {
    Optional<ShoppingCartPersistenceEntity> findByCustomer_Id(UUID customerId);
}
