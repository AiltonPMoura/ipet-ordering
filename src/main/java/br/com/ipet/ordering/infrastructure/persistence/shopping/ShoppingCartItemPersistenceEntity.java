package br.com.ipet.ordering.infrastructure.persistence.shopping;

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
@Table(name = "shopping_cart_item")
public class ShoppingCartItemPersistenceEntity {

    @Id
    @EqualsAndHashCode.Include
    private UUID id;

    @JoinColumn
    @ManyToOne(optional = false)
    private ShoppingCartPersistenceEntity shoppingCart;

    @Embedded
    private ProductEmbeddable product;

    @Column
    private Integer quantity;

    @Column
    private BigDecimal totalAmount;

}
