package br.com.ipet.ordering.infrastructure.persistence.entity;

import br.com.ipet.ordering.infrastructure.persistence.company.CompanyPersistenceEntity;
import br.com.ipet.ordering.infrastructure.persistence.customer.CustomerPersistenceEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(of = "id")
@Entity
@Table(name = "\"order\"")
public class OrderPersistenceEntity {

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

    private BigDecimal totalAmount;
    private Integer totalItems;
    private String paymentMethod;
    private String status;

    private OffsetDateTime placedAt;
    private OffsetDateTime readyAt;
    private OffsetDateTime paidAt;
    private LocalDateTime deliveringAt;
    private LocalDateTime deliveryAt;
    private LocalDateTime cancelAt;

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

}
