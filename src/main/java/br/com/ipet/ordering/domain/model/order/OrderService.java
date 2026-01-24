package br.com.ipet.ordering.domain.model.order;

import br.com.ipet.ordering.domain.model.exception.ProductIsNotEnabled;
import br.com.ipet.ordering.domain.model.exception.ProductNotFoundException;
import br.com.ipet.ordering.domain.model.exception.ProductOutOfStock;
import br.com.ipet.ordering.domain.model.repository.Products;
import br.com.ipet.ordering.domain.model.commons.Product;
import br.com.ipet.ordering.domain.model.commons.ProductId;
import br.com.ipet.ordering.domain.model.commons.Quantity;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OrderService {

    private final Products products;

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
    }

}
