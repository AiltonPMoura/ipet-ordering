package br.com.ipet.ordering.domain.model;

import br.com.ipet.ordering.domain.exception.CannotBeChangeStatusException;
import br.com.ipet.ordering.domain.exception.OrderCannotBePlacedException;
import br.com.ipet.ordering.domain.exception.OrderIsNotDraftToChangeException;
import br.com.ipet.ordering.domain.exception.OrderItemNotFoundException;
import br.com.ipet.ordering.domain.util.FieldValidator;
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
    private OffsetDateTime readyAt;
    private OffsetDateTime paidAt;
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
                 OffsetDateTime placedAt, OffsetDateTime readyAt, OffsetDateTime paidAt,
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

    public void addItem(Product product, Quantity quantity) {
        this.verifyIfChangeable();

        OrderItem orderItem = OrderItem.createNew()
                .orderId(this.id)
                .product(product)
                .quantity(quantity)
                .build();

        this.items.add(orderItem);

        recalculateTotals();
    }

    public void changeItemQuantity(OrderItemId itemId, Quantity quantity) {
        this.verifyIfChangeable();
        OrderItem orderItem = findOrderItem(itemId);
        orderItem.changeQuantity(quantity);
        recalculateTotals();
    }

    public void changeStatus(OrderStatus newStatus) {
        if (this.status.canNotChange(newStatus))
            throw new CannotBeChangeStatusException(this.status.name(), newStatus.name());

        setStatus(status);
    }

    public void changePaymentMethod(PaymentMethod paymentMethod) {
        FieldValidator.requiresNonNull("order payment method", paymentMethod);
        this.setPaymentMethod(paymentMethod);
    }

    public void place() {
        verifyIfCanChangeToPlaced();
        changeStatus(OrderStatus.PLACED);
        setPlacedAt(OffsetDateTime.now());
    }

    public void markAsPaid() {
        changeStatus(OrderStatus.PAID);
        this.setPaidAt(OffsetDateTime.now());
    }

    public void markAsReady() {
        changeStatus(OrderStatus.READY);
        this.setReadyAt(OffsetDateTime.now());
    }

    public boolean isDraft() {
        return OrderStatus.DRAFT.equals(this.status);
    }

    public boolean isPlaced() {
        return OrderStatus.PLACED.equals(this.status);
    }

    public boolean isPaid() {
        return OrderStatus.PAID.equals(this.status);
    }

    public boolean isReady() {
        return OrderStatus.READY.equals(this.status);
    }

    private void verifyIfChangeable() {
        if (!isDraft())
            throw new OrderIsNotDraftToChangeException(this.id.toString());
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

    public OffsetDateTime readyAt() {
        return readyAt;
    }

    public OffsetDateTime paidAt() {
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

    private void verifyIfCanChangeToPlaced() {
        if (this.items == null || this.items.isEmpty())
            throw OrderCannotBePlacedException.noItems(this.id.toString());

        if (paymentMethod == null)
            throw OrderCannotBePlacedException.noPaymentMethod(this.id.toString());
    }

    private OrderItem findOrderItem(OrderItemId itemId) {
        return this.items.stream()
                .filter(orderItem -> orderItem.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new OrderItemNotFoundException(this.id.toString(), itemId.id().toString()));
    }

    private void recalculateTotals() {
        var totalItemsQuantity = this.items.stream().map(item -> item.getQuantity().value())
                .reduce(0, Integer::sum);

        var totalItemsAmount = this.items.stream().map(item -> item.getTotalAmount().value())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        this.setTotalItems(new Quantity(totalItemsQuantity));
        this.setTotalAmount(new Money(totalItemsAmount));
    }

    private void setId(OrderId id) {
        FieldValidator.requiresNonNull("order id", id);
        this.id = id;
    }

    private void setCustomrtId(CustumerId custumerId) {
        FieldValidator.requiresNonNull("customer id", custumerId);
        this.custumerId = custumerId;
    }

    private void setItems(Set<OrderItem> items) {
        FieldValidator.requiresNonNull("order items", items);
        this.items = items;
    }

    private void setTotalAmount(Money totalAmount) {
        FieldValidator.requiresNonNull("order total amount", totalAmount);
        this.totalAmount = totalAmount;
    }

    private void setTotalItems(Quantity totalItems) {
        FieldValidator.requiresNonNull("order total items", totalItems);
        this.totalItems = totalItems;
    }

    private void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    private void setStatus(OrderStatus status) {
        FieldValidator.requiresNonNull("order status", status);
        this.status = status;
    }

    private void setPlacedAt(OffsetDateTime placedAt) {
        this.placedAt = placedAt;
    }

    private void setReadyAt(OffsetDateTime readyAt) {
        this.readyAt = readyAt;
    }

    private void setPaidAt(OffsetDateTime paidAt) {
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
