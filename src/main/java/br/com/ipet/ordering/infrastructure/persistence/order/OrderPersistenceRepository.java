package br.com.ipet.ordering.infrastructure.persistence.order;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderPersistenceRepository extends JpaRepository<OrderPersistenceEntity, UUID> {
}
