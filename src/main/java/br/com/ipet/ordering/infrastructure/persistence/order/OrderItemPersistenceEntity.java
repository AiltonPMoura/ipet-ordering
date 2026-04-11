package br.com.ipet.ordering.infrastructure.persistence.order;

import br.com.ipet.ordering.infrastructure.persistence.commons.ProductEmbeddable;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(of = "id")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "order_item")
public class OrderItemPersistenceEntity {

    @Id
    @EqualsAndHashCode.Include
    private UUID id;

    @JoinColumn
    @ManyToOne(optional = false)
    private OrderPersistenceEntity order;

    @Embedded
    private ProductEmbeddable product;

    @Column
    private Integer quantity;

    @Column(name = "total_amount")
    private BigDecimal totalAmount;

    public UUID getOrderId() {
        return this.order.getId();
    }

}
