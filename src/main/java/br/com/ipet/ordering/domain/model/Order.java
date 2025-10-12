package br.com.ipet.ordering.domain.model;

import br.com.ipet.ordering.domain.exception.CannotBeChangeStatusException;
import br.com.ipet.ordering.domain.exception.OrderCannotBePlacedException;
import br.com.ipet.ordering.domain.valueobject.*;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Order {
    private OrderId id;
    private CustumerId custumerId;
    private Set<OrderItem> items;
    private Money totalAmount;
    private Quantity totalItems;
    private PaymentMethod paymentMethod;
    private OrderStatus status;
    private OffsetDateTime placedAt;
    private LocalDateTime readyAt;
    private LocalDateTime paidAt;
    private LocalDateTime deliveringAt;
    private LocalDateTime deliveryAt;
    private LocalDateTime cancelAt;

    public static Order draft(CustumerId custumerId) {
        return new Order(
                new OrderId(),
                custumerId,
                new HashSet<>(),
                Money.ZERO,
                Quantity.ZERO,
                null,
                OrderStatus.DRAFT,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }

    @Builder(builderClassName = "ExistingOrderBuilder", builderMethodName = "existing")
    private Order(OrderId id, CustumerId custumerId, Set<OrderItem> items,
                 Money totalAmount, Quantity totalItems,
                  PaymentMethod paymentMethod, OrderStatus status,
                 OffsetDateTime placedAt, LocalDateTime readyAt, LocalDateTime paidAt,
                 LocalDateTime deliveringAt, LocalDateTime deliveryAt, LocalDateTime cancelAt) {
        this.setId(id);
        this.setCustomrtId(custumerId);
        this.setItems(items);
        this.setTotalAmount(totalAmount);
        this.setTotalItems(totalItems);
        this.setPaymentMethod(paymentMethod);
        this.setStatus(status);
        this.setPlacedAt(placedAt);
        this.setReadyAt(readyAt);
        this.setPaidAt(paidAt);
        this.setDeliveringAt(deliveringAt);
        this.setDeliveryAt(deliveryAt);
        this.setCancelAt(cancelAt);
    }

    public void addItems(Product product, Quantity quantity) {
        OrderItem orderItem = OrderItem.createNew()
                .orderId(this.id)
                .product(product)
                .quantity(quantity)
                .build();

        this.items.add(orderItem);

        recalculateTotals();
    }

    public void place() {
        verifyIfCanChangeToPlaced();
        changeStatus(OrderStatus.PLACED);
        setPlacedAt(OffsetDateTime.now());
    }

    private void verifyIfCanChangeToPlaced() {
        if (this.items == null || this.items.isEmpty())
            throw OrderCannotBePlacedException.noItems(this.id.toString());

        if (paymentMethod == null)
            throw OrderCannotBePlacedException.noPaymentMethod(this.id.toString());
    }

    public void changeStatus(OrderStatus newStatus) {
        if (this.status.canNotChange(newStatus))
            throw new CannotBeChangeStatusException(this.status.name(), newStatus.name());

        setStatus(status);
    }

    public OrderId id() {
        return id;
    }

    public CustumerId custumerId() {
        return custumerId;
    }

    public Set<OrderItem> items() {
        return Collections.unmodifiableSet(items);
    }

    public Money totalAmount() {
        return totalAmount;
    }

    public Quantity totalItems() {
        return totalItems;
    }

    public OrderStatus status() {
        return status;
    }

    public OffsetDateTime placedAt() {
        return placedAt;
    }

    public LocalDateTime readyAt() {
        return readyAt;
    }

    public LocalDateTime paidAt() {
        return paidAt;
    }

    public LocalDateTime deliveringAt() {
        return deliveringAt;
    }

    public LocalDateTime deliveryAt() {
        return deliveryAt;
    }

    public LocalDateTime cancelAt() {
        return cancelAt;
    }

    private void recalculateTotals() {
        var totalItemsQuantity = this.items.stream().map(item -> item.getQuantity().value())
                .reduce(0, Integer::sum);

        var totalItemsAmount = this.items.stream().map(item -> item.getTotalAmount().value())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        setTotalItems(new Quantity(totalItemsQuantity));
        setTotalAmount(new Money(totalItemsAmount));
    }

    private void setId(OrderId id) {
        this.id = id;
    }

    private void setCustomrtId(CustumerId custumerId) {
        this.custumerId = custumerId;
    }

    private void setItems(Set<OrderItem> items) {
        this.items = items;
    }

    private void setTotalAmount(Money totalAmount) {
        this.totalAmount = totalAmount;
    }

    private void setTotalItems(Quantity totalItems) {
        this.totalItems = totalItems;
    }

    private void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    private void setStatus(OrderStatus status) {
        this.status = status;
    }

    private void setPlacedAt(OffsetDateTime placedAt) {
        this.placedAt = placedAt;
    }

    private void setReadyAt(LocalDateTime readyAt) {
        this.readyAt = readyAt;
    }

    private void setPaidAt(LocalDateTime paidAt) {
        this.paidAt = paidAt;
    }

    private void setDeliveringAt(LocalDateTime deliveringAt) {
        this.deliveringAt = deliveringAt;
    }

    private void setDeliveryAt(LocalDateTime deliveryAt) {
        this.deliveryAt = deliveryAt;
    }

    private void setCancelAt(LocalDateTime cancelAt) {
        this.cancelAt = cancelAt;
    }
}
