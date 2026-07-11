package br.com.ipet.ordering.infrastructure.persistence.order;

import br.com.ipet.ordering.infrastructure.persistence.company.CompanyPersistenceEntity;
import br.com.ipet.ordering.infrastructure.persistence.customer.CustomerPersistenceEntity;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.AbstractAggregateRoot;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@ToString(of = "id")
@Entity
@Table(name = "\"order\"")
@EntityListeners(AuditingEntityListener.class)
public class OrderPersistenceEntity
        extends AbstractAggregateRoot<OrderPersistenceEntity> {

    @Id
    @EqualsAndHashCode.Include
    private UUID id;

    @JoinColumn
    @ManyToOne(optional = false)
    private CustomerPersistenceEntity customer;

    @JoinColumn
    @ManyToOne(optional = false)
    private CompanyPersistenceEntity company;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private Set<OrderItemPersistenceEntity> items = new HashSet<>();

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

    @Embedded
    @AttributeOverride(name = "cost", column = @Column(name = "shipping_cost"))
    @AttributeOverride(name = "expectedDate", column = @Column(name = "shipping_expected_date"))
    @AttributeOverride(name = "address.street", column = @Column(name = "shipping_address_street"))
    @AttributeOverride(name = "address.number", column = @Column(name = "shipping_address_number"))
    @AttributeOverride(name = "address.complement", column = @Column(name = "shipping_address_complement"))
    @AttributeOverride(name = "address.neighborhood", column = @Column(name = "shipping_address_neighborhood"))
    @AttributeOverride(name = "address.city", column = @Column(name = "shipping_address_city"))
    @AttributeOverride(name = "address.state", column = @Column(name = "shipping_address_state"))
    @AttributeOverride(name = "address.zipCode", column = @Column(name = "shipping_address_zipCode"))
    private ShippingEmbeddable shipping;

    @Embedded
    @AttributeOverride(name = "companyName", column = @Column(name = "company_name"))
    @AttributeOverride(name = "document", column = @Column(name = "company_document"))
    @AttributeOverride(name = "phone", column = @Column(name = "company_phone"))
    @AttributeOverride(name = "email", column = @Column(name = "company_email"))
    @AttributeOverride(name = "address.street", column = @Column(name = "company_address_street"))
    @AttributeOverride(name = "address.number", column = @Column(name = "company_address_number"))
    @AttributeOverride(name = "address.complement", column = @Column(name = "company_address_complement"))
    @AttributeOverride(name = "address.neighborhood", column = @Column(name = "company_address_neighborhood"))
    @AttributeOverride(name = "address.city", column = @Column(name = "company_address_city"))
    @AttributeOverride(name = "address.state", column = @Column(name = "company_address_state"))
    @AttributeOverride(name = "address.zipCode", column = @Column(name = "company_address_zipCode"))
    private DeliveryCompanyEmbeddable deliveryCompany;

    private BigDecimal totalAmount;
    private Integer totalItems;
    private String paymentMethod;
    private String status;
    private OffsetDateTime placedAt;
    private OffsetDateTime readyAt;
    private OffsetDateTime paidAt;
    private OffsetDateTime outForDeliveryAt;
    private OffsetDateTime deliveredAt;
    private OffsetDateTime canceledAt;

    @CreatedBy
    private UUID createdByUserId;

    @LastModifiedDate
    private OffsetDateTime lastModifiedAt;

    @LastModifiedBy
    private UUID lastModifiedByUserId;

    public void setItems(Set<OrderItemPersistenceEntity> items) {
        items.forEach(item -> item.setOrder(this));
        this.items = items;
    }

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
