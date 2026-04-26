package br.com.ipet.ordering.infrastructure.persistence.customer;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.AbstractAggregateRoot;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(of = "id")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Entity
@Table(name = "customer")
public class CustomerPersistenceEntity
        extends AbstractAggregateRoot<CustomerPersistenceEntity> {

    @Id
    @EqualsAndHashCode.Include
    private UUID id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private String phone;

    @Column
    private String email;

    @Column
    private String document;

    private LocalDate birthDate;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private Set<CustomerAddressPersistenceEntity> customerAddress = new HashSet<>();

    @Column
    private OffsetDateTime registerAt;

    @CreatedBy
    private UUID createdByUserId;

    @LastModifiedBy
    private UUID lastModifiedByUserId;

    @LastModifiedDate
    private OffsetDateTime lastModifiedAt;

    public void setAddress(Set<CustomerAddressPersistenceEntity> customerAddress) {
        customerAddress.forEach(address -> address.setCustomer(this));
        this.customerAddress = customerAddress;
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
