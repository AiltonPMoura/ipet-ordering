package br.com.ipet.ordering.domain.model.order;

import br.com.ipet.ordering.domain.model.FieldValidator;

import br.com.ipet.ordering.domain.model.commons.Money;
import br.com.ipet.ordering.domain.model.commons.Product;
import br.com.ipet.ordering.domain.model.commons.Quantity;
import lombok.Builder;

import java.util.Objects;

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
        this.setTotalAmount(this.product.price().multiply(this.quantity));
    }

    public OrderItemId id() {
        return id;
    }

    public OrderId orderId() {
        return orderId;
    }

    public Product product() {
        return product;
    }

    public Quantity quantity() {
        return quantity;
    }

    public Money totalAmount() {
        return totalAmount;
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem orderItem = (OrderItem) o;
        return Objects.equals(id, orderItem.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
