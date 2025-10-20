package br.com.ipet.ordering.domain.model;

import br.com.ipet.ordering.domain.util.FieldValidator;
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
        var orderItem = new OrderItem(
                new OrderItemId(),
                orderId,
                product,
                quantity,
                Money.ZERO
        );

        orderItem.reCalculateTotals();

        return orderItem;
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
        reCalculateTotals();
    }

    private void reCalculateTotals() {
        var totalAmount = this.product.price().multiply(this.quantity);
        this.setTotalAmount(totalAmount);
    }

    private void setId(OrderItemId id) {
        FieldValidator.requiresNonNull("order item id", id);
        this.id = id;
    }

    private void setOrderId(OrderId orderId) {
        FieldValidator.requiresNonNull("order id", orderId);
        this.orderId = orderId;
    }

    private void setQuantity(Quantity quantity) {
        FieldValidator.requiresNonNull("order item quantity", quantity);
        this.quantity = quantity;
    }

    private void setProduct(Product product) {
        FieldValidator.requiresNonNull("order item product", product);
        this.product = product;
    }

    private void setTotalAmount(Money totalAmount) {
        FieldValidator.requiresNonNull("order item total amount", totalAmount);
        this.totalAmount = totalAmount;
    }
}
