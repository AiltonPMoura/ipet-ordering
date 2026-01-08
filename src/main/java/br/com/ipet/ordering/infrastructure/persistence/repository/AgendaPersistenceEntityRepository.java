package br.com.ipet.ordering.infrastructure.persistence.repository;

import br.com.ipet.ordering.infrastructure.persistence.entity.AgendaPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AgendaPersistenceEntityRepository extends JpaRepository<AgendaPersistenceEntity, UUID> {
}
