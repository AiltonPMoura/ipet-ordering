package br.com.ipet.ordering.domain.model.order;

import br.com.ipet.ordering.domain.model.AbstractEventSourceEntity;
import br.com.ipet.ordering.domain.model.AggregateRoot;
import br.com.ipet.ordering.domain.model.FieldValidator;
import br.com.ipet.ordering.domain.model.commons.exception.CannotChangeStatusException;
import br.com.ipet.ordering.domain.model.commons.valueobject.Money;
import br.com.ipet.ordering.domain.model.product.Product;
import br.com.ipet.ordering.domain.model.commons.valueobject.Quantity;
import br.com.ipet.ordering.domain.model.company.CompanyId;
import br.com.ipet.ordering.domain.model.customer.CustomerId;
import br.com.ipet.ordering.domain.model.product.ProductDoesNotBelongsToCompany;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Order
        extends AbstractEventSourceEntity
        implements AggregateRoot<OrderId> {
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
    private OffsetDateTime outForDeliveryAt;
    private OffsetDateTime deliveredAt;
    private OffsetDateTime canceledAt;

    static Order draft(CustomerId customerId, CompanyId companyId) {
        return new Order(new OrderId(), customerId, companyId,
                new HashSet<>(), Money.ZERO, Quantity.ZERO,
                null, OrderStatus.DRAFT,
                null, null, null,
                null, null, null, null, null, null
        );
    }

    @Builder(builderClassName = "ExistingOrderBuilder", builderMethodName = "existing")
    private Order(OrderId id, CustomerId customerId, CompanyId companyId,
                  Set<OrderItem> items, Money totalAmount, Quantity totalItems,
                  PaymentMethod paymentMethod, OrderStatus status,
                  Shipping shipping, Billing billing, DeliveryCompany deliveryCompany,
                  OffsetDateTime placedAt, OffsetDateTime paidAt, OffsetDateTime readyAt,
                  OffsetDateTime outForDeliveryAt, OffsetDateTime deliveredAt, OffsetDateTime canceledAt) {
        this.setId(id);
        this.setCompanyId(companyId);
        this.setCustomerId(customerId);
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
        this.setOutForDeliveryAt(outForDeliveryAt);
        this.setDeliveredAt(deliveredAt);
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
        var orderItem = this.findOrderItem(itemId);
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

        if (shipping.expectedDate().isBefore(LocalDate.now()))
            throw new InvalidShippingDeliveryDateException("");

        this.setShipping(shipping);
        this.recalculateTotals();
    }

    public void changeDeliveryCompany(DeliveryCompany deliveryCompany) {
        FieldValidator.requiresNonNull("deliveryCompany", deliveryCompany);
        this.verifyIfChangeable();
        this.setDeliveryCompany(deliveryCompany);
    }

    public void place(CustomerId customerId, CompanyId companyId) {
        this.verifyIfCanChangeToPlaced(customerId, companyId);
        this.changeStatus(OrderStatus.PLACED);
        this.setPlacedAt(OffsetDateTime.now());
        this.publishDomainEvent(new OrderPlacedEvent(this.id, this.customerId, this.companyId, this.placedAt));
    }

    public void markAsPaid() {
        this.changeStatus(OrderStatus.PAID);
        this.setPaidAt(OffsetDateTime.now());
        this.publishDomainEvent(new OrderPaidEvent(this.id, this.customerId, this.companyId, this.paidAt));
    }

    public void markAsReady() {
        this.changeStatus(OrderStatus.READY);
        this.setReadyAt(OffsetDateTime.now());
        this.publishDomainEvent(new OrderReadyEvent(this.id, this.customerId, this.companyId, this.readyAt));
    }

    public void outForDelivery() {
        this.changeStatus(OrderStatus.OUT_FOR_DELIVERY);
        this.setOutForDeliveryAt(OffsetDateTime.now());
        this.publishDomainEvent(new OrderOutForDeliveredEvent(this.id, this.customerId, this.companyId, this.outForDeliveryAt));
    }

    public void delivered() {
        this.changeStatus(OrderStatus.DELIVERED);
        this.setDeliveredAt(OffsetDateTime.now());
        this.publishDomainEvent(new OrderDeliveredEvent(this.id, this.customerId, this.companyId, this.deliveredAt));
    }

    public void cancel() {
        this.changeStatus(OrderStatus.CANCELED);
        this.setCanceledAt(OffsetDateTime.now());
        this.publishDomainEvent(new OrderCanceledEvent(this.id, this.customerId, this.companyId, this.canceledAt));
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

    public boolean isOutForDelivery() {
        return OrderStatus.OUT_FOR_DELIVERY.equals(this.status);
    }

    public boolean isDelivered() {
        return OrderStatus.DELIVERED.equals(this.status);
    }

    public boolean isCancel() {
        return OrderStatus.CANCELED.equals(this.status);
    }

    private void changeStatus(OrderStatus newStatus) {
        if (this.status.canNotChangeTo(newStatus))
            throw new CannotChangeStatusException(this.status.name(), newStatus.name());

        this.setStatus(status);
    }

    private void verifyIfCanChangeToPlaced(CustomerId customerId, CompanyId companyId) {
        this.verifyIfOrderBelongsToTheCustomer(customerId);
        this.verifyOrderAssociatedWithCompany(companyId);
        this.veryfyProductsBelongsToTheCompany();

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

    private void verifyIfOrderBelongsToTheCustomer(CustomerId customerId) {
        if (!this.customerId.equals(customerId))
            throw new OrderDoesNotBelongsToTheCustomer("");
    }

    private void verifyOrderAssociatedWithCompany(CompanyId companyId) {
        if (!this.companyId.equals(companyId))
            throw new OrderDoesNotAssociatedWithCompany("");
    }

    private void veryfyProductsBelongsToTheCompany() {
        var notBelongsCompany = this.items.stream().noneMatch(item ->
                item.product().companyId().equals(this.companyId));

        if (notBelongsCompany)
            throw new ProductDoesNotBelongsToCompany("");
    }

    public OrderId id() {
        return id;
    }

    private void setId(OrderId id) {
        FieldValidator.requiresNonNull("orderId", id);
        this.id = id;
    }

    public CustomerId customerId() {
        return customerId;
    }

    private void setCustomerId(CustomerId customerId) {
        FieldValidator.requiresNonNull("customerId", customerId);
        this.customerId = customerId;
    }

    public CompanyId companyId() {
        return companyId;
    }

    private void setCompanyId(CompanyId companyId) {
        FieldValidator.requiresNonNull("companyId", companyId);
        this.companyId = companyId;
    }

    public Set<OrderItem> items() {
        return Collections.unmodifiableSet(items);
    }

    private void setItems(Set<OrderItem> items) {
        FieldValidator.requiresNonNull("order items", items);
        this.items = items;
    }

    public Money totalAmount() {
        return totalAmount;
    }

    private void setTotalAmount(Money totalAmount) {
        FieldValidator.requiresNonNull("order total amount", totalAmount);
        this.totalAmount = totalAmount;
    }

    public Quantity totalItems() {
        return totalItems;
    }

    private void setTotalItems(Quantity totalItems) {
        FieldValidator.requiresNonNull("order total items", totalItems);
        this.totalItems = totalItems;
    }

    public PaymentMethod paymentMethod() {
        return paymentMethod;
    }

    private void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public OrderStatus status() {
        return status;
    }

    private void setStatus(OrderStatus status) {
        FieldValidator.requiresNonNull("order status", status);
        this.status = status;
    }

    public Shipping shipping() {
        return shipping;
    }

    private void setShipping(Shipping shipping) {
        this.shipping = shipping;
    }

    public Billing billing() {
        return billing;
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

    public OffsetDateTime placedAt() {
        return placedAt;
    }

    private void setPlacedAt(OffsetDateTime placedAt) {
        this.placedAt = placedAt;
    }

    public OffsetDateTime readyAt() {
        return readyAt;
    }

    private void setReadyAt(OffsetDateTime readyAt) {
        this.readyAt = readyAt;
    }

    public OffsetDateTime paidAt() {
        return paidAt;
    }

    private void setPaidAt(OffsetDateTime paidAt) {
        this.paidAt = paidAt;
    }

    public OffsetDateTime outForDeliveryAt() {
        return outForDeliveryAt;
    }

    private void setOutForDeliveryAt(OffsetDateTime outForDeliveryAt) {
        this.outForDeliveryAt = outForDeliveryAt;
    }

    public OffsetDateTime deliveredAt() {
        return deliveredAt;
    }

    private void setDeliveredAt(OffsetDateTime deliveredAt) {
        this.deliveredAt = deliveredAt;
    }

    public OffsetDateTime canceledAt() {
        return canceledAt;
    }

    private void setCanceledAt(OffsetDateTime canceledAt) {
        this.canceledAt = canceledAt;
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
