package br.com.ipet.ordering.domain.model.order;

import br.com.ipet.ordering.domain.model.commons.valueobject.Product;
import br.com.ipet.ordering.domain.model.product.ProductId;
import br.com.ipet.ordering.domain.model.commons.valueobject.Quantity;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OrderService {

    /*private final Products products;

    public void addItem(Order order, Quantity quantity, ProductId productId) {

        var product = products.ofId(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId.toString()));

        if (!product.availableStock())
            throw new ProductOutOfStock();

        if (!product.enabled())
            throw new ProductIsNotEnabled();

        order.addItem(
                new Product(product.id(), product.name(), product.description(), product.price()),
                quantity
        );
    }*/

}
