package br.com.ipet.ordering.infrastructure.persistence.entity;

import br.com.ipet.ordering.domain.model.entity.OrderItem;
import br.com.ipet.ordering.domain.model.entity.OrderService;
import br.com.ipet.ordering.domain.model.entity.OrderStatus;
import br.com.ipet.ordering.domain.model.entity.PaymentMethod;
import br.com.ipet.ordering.domain.model.valueobject.Money;
import br.com.ipet.ordering.domain.model.valueobject.Quantity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = false)
@ToString(of = "id")
@Entity
@Table(name = "\"ORDER\"")
public class OrderPersistenceEntity {

    @Id
    @EqualsAndHashCode.Include
    private UUID id;

    private UUID customerId;

    private UUID companyId;

    /*@OneToMany
    private Set<OrderItemPersistenceEntity> items;*/

    //private Set<OrderService> services;

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

}
