package br.com.ipet.ordering.infrastructure.persistence.repository;

import br.com.ipet.ordering.infrastructure.persistence.entity.BookingPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

public interface BookingPersistenceEntityRepository extends JpaRepository<BookingPersistenceEntity, UUID> {

    //Set<BookingPersistenceEntity> findByAgendaIdAndDateGreaterThan(UUID id, LocalDate date);

}
