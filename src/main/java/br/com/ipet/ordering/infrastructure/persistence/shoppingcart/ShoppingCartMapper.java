package br.com.ipet.ordering.infrastructure.persistence.shoppingcart;

import br.com.ipet.ordering.domain.model.commons.valueobject.Money;
import br.com.ipet.ordering.domain.model.commons.valueobject.Quantity;
import br.com.ipet.ordering.domain.model.customer.CustomerId;
import br.com.ipet.ordering.domain.model.product.Product;
import br.com.ipet.ordering.domain.model.product.ProductDescription;
import br.com.ipet.ordering.domain.model.product.ProductId;
import br.com.ipet.ordering.domain.model.product.ProductName;
import br.com.ipet.ordering.domain.model.shoppingcart.ShoppingCart;
import br.com.ipet.ordering.domain.model.shoppingcart.ShoppingCartId;
import br.com.ipet.ordering.domain.model.shoppingcart.ShoppingCartItem;
import br.com.ipet.ordering.domain.model.shoppingcart.ShoppingCartItemId;
import br.com.ipet.ordering.infrastructure.persistence.commons.ProductEmbeddable;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ShoppingCartMapper {

    public ShoppingCart toDomain(ShoppingCartPersistenceEntity shoppingCartPersistence) {
        return ShoppingCart.existing()
                .id(new ShoppingCartId(shoppingCartPersistence.getId()))
                .customerId(new CustomerId(shoppingCartPersistence.getCustomerId()))
                .items(this.toItemDomain(shoppingCartPersistence.getItems()))
                .totalItems(new Quantity(shoppingCartPersistence.getTotalItems()))
                .totalAmount(new Money(shoppingCartPersistence.getTotalAmount()))
                .createdAt(shoppingCartPersistence.getCreatedAt())
                .build();
    }

    private Set<ShoppingCartItem> toItemDomain(Set<ShoppingCartItemPersistenceEntity> itemsPersistence) {
        return itemsPersistence.stream().map(itemPersistence ->
                ShoppingCartItem.existing()
                        .id(new ShoppingCartItemId(itemPersistence.getId()))
                        .shoppingCartId(new ShoppingCartId(itemPersistence.getShoppingCartId()))
                        .product(this.toProduct(itemPersistence.getProduct()))
                        .quantity(new Quantity(itemPersistence.getQuantity()))
                        .totalAmount(new Money(itemPersistence.getTotalAmount()))
                        .build()
        ).collect(Collectors.toSet());
    }

    private Product toProduct(ProductEmbeddable productEmbeddable) {
        return Product.builder()
                .id(new ProductId(productEmbeddable.getProductId()))
                .name(new ProductName(productEmbeddable.getName()))
                .description(new ProductDescription(productEmbeddable.getDescription()))
                .price(new Money(productEmbeddable.getPrice()))
                .build();
    }

}
