package br.com.ipet.ordering.domain.model.order;

import br.com.ipet.ordering.domain.model.AggregateRoot;
import br.com.ipet.ordering.domain.model.FieldValidator;
import br.com.ipet.ordering.domain.model.commons.exception.CannotChangeStatusException;
import br.com.ipet.ordering.domain.model.commons.valueobject.Money;
import br.com.ipet.ordering.domain.model.commons.valueobject.Product;
import br.com.ipet.ordering.domain.model.commons.valueobject.Quantity;
import br.com.ipet.ordering.domain.model.company.CompanyId;
import br.com.ipet.ordering.domain.model.customer.CustomerId;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Order implements AggregateRoot<OrderId> {
    private OrderId id;
    private CustomerId customerId;
    private CompanyId companyId;
    private Set<OrderItem> items;
    private Money totalAmount;
    private Quantity totalItems;
    private PaymentMethod paymentMethod;
    private OrderStatus status;
    private Shipping shipping;
    private Billing billing;
    private DeliveryCompany deliveryCompany;
    private OffsetDateTime placedAt;
    private OffsetDateTime paidAt;
    private OffsetDateTime readyAt;
    private OffsetDateTime completedAt;
    private OffsetDateTime canceledAt;

    public static Order draft(CustomerId customerId, CompanyId companyId) {
        return new Order(new OrderId(), customerId, companyId,
                new HashSet<>(), Money.ZERO, Quantity.ZERO,
                null, OrderStatus.DRAFT,
                null, null, null,
                null, null, null, null, null
        );
    }

    @Builder(builderClassName = "ExistingOrderBuilder", builderMethodName = "existing")
    private Order(OrderId id, CustomerId customerId, CompanyId companyId,
                  Set<OrderItem> items, Money totalAmount, Quantity totalItems,
                  PaymentMethod paymentMethod, OrderStatus status,
                  Shipping shipping, Billing billing, DeliveryCompany deliveryCompany,
                  OffsetDateTime placedAt, OffsetDateTime paidAt, OffsetDateTime readyAt,
                  OffsetDateTime completedAt, OffsetDateTime canceledAt) {
        this.setId(id);
        this.setCompanyId(companyId);
        this.setCustomrtId(customerId);
        this.setItems(items);
        this.setTotalAmount(totalAmount);
        this.setTotalItems(totalItems);
        this.setPaymentMethod(paymentMethod);
        this.setStatus(status);
        this.setShipping(shipping);
        this.setBilling(billing);
        this.setDeliveryCompany(deliveryCompany);
        this.setPlacedAt(placedAt);
        this.setPaidAt(paidAt);
        this.setReadyAt(readyAt);
        this.setCompletedAt(completedAt);
        this.setCanceledAt(canceledAt);
    }

    public void addItem(Product product, Quantity quantity) {
        this.verifyIfChangeable();

        var orderItem = OrderItem.createNew()
                .orderId(this.id)
                .product(product)
                .quantity(quantity)
                .build();

        this.items.add(orderItem);

        this.recalculateTotals();
    }

    public void removeItem(OrderItemId itemId) {
        this.verifyIfChangeable();

        var orderItem = this.findOrderItem(itemId);

        this.items.remove(orderItem);

        this.recalculateTotals();
    }

    public void changeItemQuantity(OrderItemId itemId, Quantity quantity) {
        this.verifyIfChangeable();

        var orderItem = findOrderItem(itemId);
        orderItem.changeQuantity(quantity);

        this.recalculateTotals();
    }

    public void changePaymentMethod(PaymentMethod paymentMethod) {
        FieldValidator.requiresNonNull("paymentMethod", paymentMethod);
        this.verifyIfChangeable();
        this.setPaymentMethod(paymentMethod);
    }

    public void changeBilling(Billing billing) {
        FieldValidator.requiresNonNull("billing", billing);
        this.verifyIfChangeable();
        this.setBilling(billing);
    }

    public void changeShipping(Shipping shipping) {
        FieldValidator.requiresNonNull("shipping", shipping);
        this.verifyIfChangeable();

        if (shipping.expetedDate().isBefore(LocalDate.now()))
            throw new InvalidShippingDeliveryDateException("");

        this.setShipping(shipping);
    }

    public void changeDeliveryCompany(DeliveryCompany deliveryCompany) {
        FieldValidator.requiresNonNull("deliveryCompany", deliveryCompany);
        this.verifyIfChangeable();
        this.setDeliveryCompany(deliveryCompany);
    }

    public void place() {
        this.verifyIfCanChangeToPlaced();
        this.changeStatus(OrderStatus.PLACED);
        this.setPlacedAt(OffsetDateTime.now());
    }

    public void markAsPaid() {
        this.changeStatus(OrderStatus.PAID);
        this.setPaidAt(OffsetDateTime.now());
    }

    public void markAsReady() {
        this.changeStatus(OrderStatus.READY);
        this.setReadyAt(OffsetDateTime.now());
    }

    public void cancel() {
        this.changeStatus(OrderStatus.CANCELED);
        this.setCanceledAt(OffsetDateTime.now());
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

    public boolean isCancel() {
        return OrderStatus.CANCELED.equals(this.status);
    }

    private void changeStatus(OrderStatus newStatus) {
        if (this.status.canNotChangeTo(newStatus))
            throw new CannotChangeStatusException(this.status.name(), newStatus.name());

        this.setStatus(status);
    }

    private void verifyIfCanChangeToPlaced() {
        if (this.items.isEmpty())
            throw OrderCannotBePlacedException.noItems(this.id.toString());

        if (this.paymentMethod == null)
            throw OrderCannotBePlacedException.noPaymentMethod(this.id.toString());

        if (this.shipping == null)
            throw OrderCannotBePlacedException.noShipping(this.id.toString());

        if (this.billing == null)
            throw OrderCannotBePlacedException.noBilling(this.id.toString());

        if (this.deliveryCompany == null)
            throw OrderCannotBePlacedException.noDeliveryCompany(this.id.toString());
    }

    private OrderItem findOrderItem(OrderItemId itemId) {
        FieldValidator.requiresNonNull("itemId", itemId);

        return this.items.stream()
                .filter(orderItem -> orderItem.id().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new OrderDoesNotContainItemException(this.id.value().toString(), itemId.value().toString()));
    }

    private void recalculateTotals() {
        var totalItemsQuantity = this.items.stream().map(item -> item.quantity().value())
                .reduce(0, Integer::sum);

        var totalItemsAmount = this.items.stream().map(item -> item.totalAmount().value())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal shippingCost;
        if (this.shipping == null) {
            shippingCost = BigDecimal.ZERO;
        } else {
            shippingCost = this.shipping.cost().value();
        }

        var totalItemsShippingAmount = totalItemsAmount.add(shippingCost);

        this.setTotalItems(new Quantity(totalItemsQuantity));
        this.setTotalAmount(new Money(totalItemsShippingAmount));
    }

    private void verifyIfChangeable() {
        if (!isDraft())
            throw new OrderCannotBeEditedException(this.id.toString());
    }

    private void setId(OrderId id) {
        FieldValidator.requiresNonNull("orderId", id);
        this.id = id;
    }

    private void setCustomrtId(CustomerId customerId) {
        FieldValidator.requiresNonNull("customerId", customerId);
        this.customerId = customerId;
    }

    private void setCompanyId(CompanyId companyId) {
        FieldValidator.requiresNonNull("companyId", companyId);
        this.companyId = companyId;
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

    private void setShipping(Shipping shipping) {
        this.shipping = shipping;
    }

    private void setBilling(Billing billing) {
        this.billing = billing;
    }

    public DeliveryCompany deliveryCompany() {
        return deliveryCompany;
    }

    private void setDeliveryCompany(DeliveryCompany deliveryCompany) {
        this.deliveryCompany = deliveryCompany;
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

    private void setCompletedAt(OffsetDateTime completedAt) {
        this.completedAt = completedAt;
    }

    private void setCanceledAt(OffsetDateTime canceledAt) {
        this.canceledAt = canceledAt;
    }

    public OrderId id() {
        return id;
    }

    public CustomerId custumerId() {
        return customerId;
    }

    public CompanyId ccompanyId() {
        return companyId;
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

    public Billing billing() {
        return billing;
    }

    public Shipping shpping() {
        return shipping;
    }

    public OrderStatus status() {
        return status;
    }

    public PaymentMethod paymentMethod() {
        return paymentMethod;
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

    public OffsetDateTime completedAt() {
        return completedAt;
    }

    public OffsetDateTime cancelAt() {
        return canceledAt;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Order order)) return false;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
