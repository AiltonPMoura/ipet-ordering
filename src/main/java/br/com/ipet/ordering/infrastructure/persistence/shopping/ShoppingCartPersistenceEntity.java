package br.com.ipet.ordering.infrastructure.persistence.shopping;

import br.com.ipet.ordering.infrastructure.persistence.customer.CustomerPersistenceEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(of = "id")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "shopping_cart")
public class ShoppingCartPersistenceEntity {

    @Id
    @EqualsAndHashCode.Include
    private UUID id;

    @JoinColumn
    @ManyToOne(optional = false)
    private CustomerPersistenceEntity customer;

    @OneToMany(mappedBy = "shoppingCart", cascade = CascadeType.PERSIST)
    private Set<ShoppingCartItemPersistenceEntity> items;

    @Column
    private Integer totalItems;

    @Column
    private BigDecimal totalAmount;

    @Column
    private LocalDate startAt;

    public UUID getCustomerId() {
        return this.customer.getId();
    }

    public void refreshItems() {
        this.items.forEach(item -> item.setShoppingCart(this));
    }

}
