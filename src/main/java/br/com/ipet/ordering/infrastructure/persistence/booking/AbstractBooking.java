package br.com.ipet.ordering.infrastructure.persistence.booking;

import br.com.ipet.ordering.infrastructure.persistence.company.CompanyPersistenceEntity;
import br.com.ipet.ordering.infrastructure.persistence.customer.CustomerPersistenceEntity;
import br.com.ipet.ordering.infrastructure.persistence.order.BillingEmbeddable;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.AbstractAggregateRoot;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@ToString(of = "id")
@MappedSuperclass
public class AbstractBooking extends AbstractAggregateRoot<AbstractBooking> {

    @Id
    @EqualsAndHashCode.Include
    private UUID id;

    @JoinColumn
    @ManyToOne(optional = false)
    private CustomerPersistenceEntity customer;

    @JoinColumn
    @ManyToOne(optional = false)
    private CompanyPersistenceEntity company;

    private Integer totalPets;
    private BigDecimal totalAmount;
    private String paymentMethod;
    private String paymentStatus;

    @Embedded
    @AttributeOverride(name = "firstName", column = @Column(name = "billing_first_name"))
    @AttributeOverride(name = "lastName", column = @Column(name = "billing_last_name"))
    @AttributeOverride(name = "document", column = @Column(name = "billing_document"))
    @AttributeOverride(name = "phone", column = @Column(name = "billing_phone"))
    @AttributeOverride(name = "email", column = @Column(name = "billing_email"))
    @AttributeOverride(name = "address.street", column = @Column(name = "billing_address_street"))
    @AttributeOverride(name = "address.number", column = @Column(name = "billing_address_number"))
    @AttributeOverride(name = "address.complement", column = @Column(name = "billing_address_complement"))
    @AttributeOverride(name = "address.neighborhood", column = @Column(name = "billing_address_neighborhood"))
    @AttributeOverride(name = "address.city", column = @Column(name = "billing_address_city"))
    @AttributeOverride(name = "address.state", column = @Column(name = "billing_address_state"))
    @AttributeOverride(name = "address.zipCode", column = @Column(name = "billing_address_zipCode"))
    private BillingEmbeddable billing;

    private OffsetDateTime requestedAt;
    private OffsetDateTime paidAt;
    private OffsetDateTime scheduledAt;
    private OffsetDateTime completedAt;
    private OffsetDateTime canceledAt;
    private OffsetDateTime refundedAt;
    private String cancelationReason;

    public UUID getCustomerId() {
        return this.customer.getId();
    }

    public UUID getCompanyId() {
        return this.company.getId();
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
