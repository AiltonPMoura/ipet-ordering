package br.com.ipet.ordering.infrastructure.persistence.order;

import br.com.ipet.ordering.infrastructure.persistence.entity.OrderPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderPersistenceEntityRepository extends JpaRepository<OrderPersistenceEntity, UUID> {
}
