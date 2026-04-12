package br.com.ipet.ordering.application.shoppingcart;

import br.com.ipet.ordering.domain.model.FieldValidator;
import br.com.ipet.ordering.domain.model.commons.valueobject.Quantity;
import br.com.ipet.ordering.domain.model.customer.CustomerId;
import br.com.ipet.ordering.domain.model.product.Product;
import br.com.ipet.ordering.domain.model.product.ProductCatalogService;
import br.com.ipet.ordering.domain.model.product.ProductId;
import br.com.ipet.ordering.domain.model.product.ProductNotFoundException;
import br.com.ipet.ordering.domain.model.shoppingcart.ShoppingCart;
import br.com.ipet.ordering.domain.model.shoppingcart.ShoppingCartId;
import br.com.ipet.ordering.domain.model.shoppingcart.ShoppingCartItemId;
import br.com.ipet.ordering.domain.model.shoppingcart.ShoppingCartNotFoundException;
import br.com.ipet.ordering.domain.model.shoppingcart.ShoppingCarts;
import br.com.ipet.ordering.domain.model.shoppingcart.ShoppingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ShoppingCartManagementApplicationService {

    private final ShoppingService shoppingService;
    private final ShoppingCarts shoppingCarts;
    private final ProductCatalogService productCatalogService;

    public UUID create(UUID customerId) {
        FieldValidator.requiresNonNull("customerId", customerId);

        var shoppingCart = shoppingService.startShopping(new CustomerId(customerId));

        shoppingCarts.add(shoppingCart);

        return shoppingCart.id().value();
    }

    public void addItem(UUID shoppingCartId, UUID productId, Integer quantity, UUID customerId) {
        FieldValidator.requiresNonNull("shoppingCartId", shoppingCartId);
        FieldValidator.requiresNonNull("productId", productId);
        FieldValidator.requiresNonNull("quantity", quantity);
        FieldValidator.requiresNonNull("customerId", customerId);

        var shoppingCart = this.findShoppingCart(shoppingCartId);

        var product = productCatalogService.ofId(new ProductId(productId))
                .orElseThrow(() -> new ProductNotFoundException(""));

        shoppingService.addItem(shoppingCart, product, new Quantity(quantity), new CustomerId(customerId));

        shoppingCarts.add(shoppingCart);
    }

    public void removeItem(UUID shoppingCartId, UUID shoppingCartItemId, UUID customerId) {
        FieldValidator.requiresNonNull("shoppingCartId", shoppingCartId);
        FieldValidator.requiresNonNull("shoppingCartItemId", shoppingCartItemId);
        FieldValidator.requiresNonNull("customerId", customerId);

        var shoppingCart = this.findShoppingCart(shoppingCartId);

        shoppingService.removeItem(shoppingCart, new ShoppingCartItemId(shoppingCartItemId), new CustomerId(customerId));

        shoppingCarts.add(shoppingCart);
    }

    public void empty(UUID shoppingCartId, UUID customerId) {
        FieldValidator.requiresNonNull("shoppingCartId", shoppingCartId);
        FieldValidator.requiresNonNull("customerId", customerId);

        var shoppingCart = this.findShoppingCart(shoppingCartId);

        shoppingService.empty(shoppingCart, new CustomerId(customerId));

        shoppingCarts.add(shoppingCart);
    }

    public void remove(UUID shoppingCartId, UUID customerId) {
        FieldValidator.requiresNonNull("shoppingCartId", shoppingCartId);
        FieldValidator.requiresNonNull("customerId", customerId);

        var shoppingCart = this.findShoppingCart(shoppingCartId);

        shoppingService.remove(shoppingCart, new CustomerId(customerId));

        shoppingCarts.remove(shoppingCart);
    }

    private ShoppingCart findShoppingCart(UUID shoppingCartId) {
        return shoppingCarts.ofId(new ShoppingCartId(shoppingCartId))
                .orElseThrow(() -> new ShoppingCartNotFoundException(""));
    }

}
