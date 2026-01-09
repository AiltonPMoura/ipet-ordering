package br.com.ipet.ordering.infrastructure.persistence.entity;

import br.com.ipet.ordering.domain.model.entity.Agenda;
import br.com.ipet.ordering.domain.model.entity.Customer;
import br.com.ipet.ordering.domain.model.valueobject.AgendaId;
import br.com.ipet.ordering.domain.model.valueobject.CustomerId;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.OffsetTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(of = "id")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "agenda")
public class BookingPersistenceEntity {

    @Id
    private UUID id;

    @ManyToOne(optional = false)
    private AgendaPersistenceEntity agenda;

    @ManyToOne(optional = false)
    private CustomerPersistenceEntity customer;

    private LocalDate date;
    private OffsetTime startTime;
    private OffsetTime endTime;

    public UUID getAgendaId() {
        return this.agenda.getId();
    }

    public UUID getCustomerId() {
        return this.customer.getId();
    }

}
