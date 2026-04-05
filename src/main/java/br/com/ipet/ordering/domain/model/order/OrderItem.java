package br.com.ipet.ordering.domain.model.order;

import br.com.ipet.ordering.domain.model.FieldValidator;

import br.com.ipet.ordering.domain.model.commons.valueobject.Money;
import br.com.ipet.ordering.domain.model.commons.valueobject.Product;
import br.com.ipet.ordering.domain.model.commons.valueobject.Quantity;
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
    private OrderItem(OrderItemId id, OrderId orderId,
                      Product product, Quantity quantity, Money totalAmount) {
        setId(id);
        setOrderId(orderId);
        setProduct(product);
        setQuantity(quantity);
        setTotalAmount(totalAmount);
    }

    void changeQuantity(Quantity quantity) {
        FieldValidator.requiresNonNull("quantity", quantity);

        this.setQuantity(quantity);
        this.reCalculateTotals();
    }

    private void reCalculateTotals() {
        this.setTotalAmount(this.product.price().multiply(this.quantity));
    }

    public OrderItemId id() {
        return id;
    }

    private void setId(OrderItemId id) {
        FieldValidator.requiresNonNull("id", id);
        this.id = id;
    }

    public OrderId orderId() {
        return orderId;
    }

    private void setOrderId(OrderId orderId) {
        FieldValidator.requiresNonNull("orderId", orderId);
        this.orderId = orderId;
    }

    public Quantity quantity() {
        return quantity;
    }

    private void setQuantity(Quantity quantity) {
        FieldValidator.requiresNonNull("quantity", quantity);
        this.quantity = quantity;
    }

    public Product product() {
        return product;
    }

    private void setProduct(Product product) {
        FieldValidator.requiresNonNull("product", product);
        this.product = product;
    }

    public Money totalAmount() {
        return totalAmount;
    }

    private void setTotalAmount(Money totalAmount) {
        FieldValidator.requiresNonNull("totalAmount", totalAmount);
        this.totalAmount = totalAmount;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof OrderItem orderItem)) return false;
        return Objects.equals(id, orderItem.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
