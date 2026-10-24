package br.com.ipet.ordering.infrastructure.persistence.transport;

import br.com.ipet.ordering.infrastructure.persistence.company.CompanyPersistenceEntity;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.AbstractAggregateRoot;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(of = "id")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Entity
@Table(name = "pet_transport")
public class PetTransportPersistenceEntity extends AbstractAggregateRoot<PetTransportPersistenceEntity> {

    @Id
    @EqualsAndHashCode.Include
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn
    private CompanyPersistenceEntity company;

    @Embedded
    @AttributeOverride(name = "firstName", column = @Column(name = "driver_first_name"))
    @AttributeOverride(name = "lastName", column = @Column(name = "driver_last_name"))
    @AttributeOverride(name = "document", column = @Column(name = "driver_document"))
    private DriverEmbeddable driver;

    @Embedded
    @AttributeOverride(name = "licensePlate", column = @Column(name = "vehicle_license_plate"))
    @AttributeOverride(name = "brand", column = @Column(name = "vehicle_brand"))
    @AttributeOverride(name = "model", column = @Column(name = "vehicle_model"))
    private VehicleEmbeddable vehicle;

    @Column(name = "pet_capacity")
    private Integer petCapacity;

    @Column(name = "registered_at")
    private OffsetDateTime registeredAt;

    public UUID getCompanyId() {
        return company.getId();
    }

    public Collection<Object> getEvents() {
        return super.domainEvents();
    }

    public void addEvents(Collection<Object> events) {
        if (events != null)
            for (Object event : events)
                this.registerEvent(event);
    }
}
