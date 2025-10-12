package br.com.ipet.ordering.domain.model;

import br.com.ipet.ordering.domain.valueobject.*;
import lombok.Builder;
import lombok.Getter;

@Getter
public class OrderItem {
    private OrderItemId id;
    private OrderId orderId;
    private Product product;
    private Quantity quantity;
    private Money totalAmount;

    @Builder(builderClassName = "CreateOrderItemBuilder", builderMethodName = "createNew")
    private static OrderItem create(OrderId orderId, Product product, Quantity quantity) {
        return new OrderItem(
                new OrderItemId(),
                orderId,
                product,
                quantity,
                calculateTotals(product, quantity)
        );
    }

    @Builder(builderClassName = "ExistingOrderItemBuilder", builderMethodName = "existing")
    private OrderItem(OrderItemId id, OrderId orderId, Product product, Quantity quantity, Money totalAmount) {
        setId(id);
        setOrderId(orderId);
        setProduct(product);
        setQuantity(quantity);
        setTotalAmount(totalAmount);
    }

    void changeQuantity(Quantity quantity) {
        this.setQuantity(quantity);
    }

    private static Money calculateTotals(Product product, Quantity quantity) {
        return product.price().multiply(quantity);
    }

    private void setId(OrderItemId id) {
        this.id = id;
    }

    private void setOrderId(OrderId orderId) {
        this.orderId = orderId;
    }

    private void setQuantity(Quantity quantity) {
        this.quantity = quantity;
    }

    private void setProduct(Product product) {
        this.product = product;
    }

    private void setTotalAmount(Money totalAmount) {
        this.totalAmount = totalAmount;
    }
}
