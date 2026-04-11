package br.com.ipet.ordering.infrastructure.persistence.shopping;

import br.com.ipet.ordering.domain.model.commons.valueobject.Product;
import br.com.ipet.ordering.domain.model.shopping.ShoppingCart;
import br.com.ipet.ordering.domain.model.shopping.ShoppingCartItem;
import br.com.ipet.ordering.infrastructure.persistence.commons.ProductEmbeddable;
import br.com.ipet.ordering.infrastructure.persistence.customer.CustomerPersistenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ShoppingCartPersistenceMapper {

    private final CustomerPersistenceRepository customerPersistenceRepository;

    public ShoppingCartPersistenceEntity fromDomain(ShoppingCart shoppingCart) {
        return merge(new ShoppingCartPersistenceEntity(), shoppingCart);
    }

    public ShoppingCartPersistenceEntity merge(ShoppingCartPersistenceEntity shoppingCartPersistence, ShoppingCart shoppingCart) {
        shoppingCartPersistence.setId(shoppingCart.id().value());
        shoppingCartPersistence.setCustomer(customerPersistenceRepository.getReferenceById(shoppingCart.customerId().value()));
        shoppingCartPersistence.setTotalAmount(shoppingCart.totalAmount().value());
        shoppingCartPersistence.setTotalItems(shoppingCart.totalItems().value());
        shoppingCartPersistence.setCreatedAt(shoppingCart.createdAt());
        shoppingCartPersistence.setItems(this.mergeItems(shoppingCartPersistence, shoppingCart));
        return shoppingCartPersistence;
    }

    private Set<ShoppingCartItemPersistenceEntity> mergeItems(ShoppingCartPersistenceEntity shoppingCartPersistence, ShoppingCart shoppingCart){
        var shoppingCartItemsPersistence = shoppingCartPersistence.getItems();
        var shoppingCartItems = shoppingCart.items();

        if(shoppingCartItemsPersistence.isEmpty())
            return shoppingCartItems.stream().map(this::fromDomainItem).collect(Collectors.toSet());

        var shoppingCartItemPersistenceMap = shoppingCartItemsPersistence.stream()
                .collect(Collectors.toMap(ShoppingCartItemPersistenceEntity::getId, item -> item));

        return shoppingCartItems.stream().map(shoppingCartItem -> {
            var shoppingCartItemPersistence = shoppingCartItemPersistenceMap.getOrDefault(shoppingCartItem.id().value(), new ShoppingCartItemPersistenceEntity());
            return mergeItem(shoppingCartItemPersistence, shoppingCartItem);
        }).collect(Collectors.toSet());
    }

    private ShoppingCartItemPersistenceEntity fromDomainItem(ShoppingCartItem shoppingCartItem) {
        return mergeItem(new ShoppingCartItemPersistenceEntity(), shoppingCartItem);
    }

    private ShoppingCartItemPersistenceEntity mergeItem(ShoppingCartItemPersistenceEntity shoppingCartItemPersistence, ShoppingCartItem shoppingCartItem) {
        shoppingCartItemPersistence.setId(shoppingCartItem.id().value());
        shoppingCartItemPersistence.setProduct(this.toProductEmbeddable(shoppingCartItem.product()));
        shoppingCartItemPersistence.setQuantity(shoppingCartItem.quantity().value());
        shoppingCartItemPersistence.setTotalAmount(shoppingCartItem.totalAmount().value());
        return shoppingCartItemPersistence;
    }

    private ProductEmbeddable toProductEmbeddable(Product product) {
        return ProductEmbeddable.builder()
                .productId(product.id().value())
                .name(product.name().value())
                .description(product.description().value())
                .price(product.price().value())
                .build();
    }
}
