package br.com.ipet.ordering.application.shoppingcart.management;

import br.com.ipet.ordering.domain.model.FieldValidator;
import br.com.ipet.ordering.domain.model.commons.valueobject.Quantity;
import br.com.ipet.ordering.domain.model.customer.CustomerId;
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
        var shoppingCart = shoppingService.startShopping(new CustomerId(customerId));
        shoppingCarts.add(shoppingCart);

        return shoppingCart.id().value();
    }

    public void addItem(UUID customerId, UUID shoppingCartId, ShoppingCartItemInput input) {
        FieldValidator.requiresNonNull("input", input);

        var shoppingCart = this.findShoppingCart(shoppingCartId);

        var product = productCatalogService.ofId(new ProductId(input.getProductId()))
                .orElseThrow(() -> new ProductNotFoundException(""));

        shoppingCart.addItem(product, new Quantity(input.getQuantity()), new CustomerId(customerId));

        shoppingCarts.add(shoppingCart);
    }

    public void removeItem(UUID shoppingCartId, UUID shoppingCartItemId, UUID customerId) {
        var shoppingCart = this.findShoppingCart(shoppingCartId);
        shoppingCart.removeItem(new ShoppingCartItemId(shoppingCartItemId), new CustomerId(customerId));

        shoppingCarts.add(shoppingCart);
    }

    public void empty(UUID shoppingCartId, UUID customerId) {
        var shoppingCart = this.findShoppingCart(shoppingCartId);
        shoppingCart.empty(new CustomerId(customerId));

        shoppingCarts.add(shoppingCart);
    }

    public void delete(UUID shoppingCartId, UUID customerId) {
        var shoppingCart = this.findShoppingCart(shoppingCartId);
        shoppingCart.discard(new CustomerId(customerId));

        shoppingCarts.remove(shoppingCart);
    }

    private ShoppingCart findShoppingCart(UUID shoppingCartId) {
        return shoppingCarts.ofId(new ShoppingCartId(shoppingCartId))
                .orElseThrow(() -> new ShoppingCartNotFoundException(""));
    }

}
