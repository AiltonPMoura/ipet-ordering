package br.com.ipet.ordering.domain.model.shopping;

import br.com.ipet.ordering.domain.model.AggregateRoot;
import br.com.ipet.ordering.domain.model.FieldValidator;
import br.com.ipet.ordering.domain.model.commons.valueobject.Money;
import br.com.ipet.ordering.domain.model.commons.valueobject.Product;
import br.com.ipet.ordering.domain.model.commons.valueobject.Quantity;
import br.com.ipet.ordering.domain.model.customer.CustomerId;
import br.com.ipet.ordering.domain.model.product.ProductId;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ShoppingCart implements AggregateRoot<ShoppingCartId> {

    private ShoppingCartId id;
    private Set<ShoppingCartItem> items;
    private Quantity totalItems;
    private Money totalAmount;
    private LocalDate startAt;

    public static ShoppingCart startShopping(CustomerId customerId) {
        return new ShoppingCart(new ShoppingCartId(), customerId,
                new HashSet<>(), Quantity.ZERO, Money.ZERO, LocalDate.now());
    }

    @Builder(builderClassName = "CreateShoppingCartBuilder", builderMethodName = "existing")
    private ShoppingCart(ShoppingCartId id, CustomerId customerId, Set<ShoppingCartItem> items,
                         Quantity totalItems, Money totalAmount, LocalDate startAt) {
        this.setId(id);
        this.setItems(items);
        this.setTotalItems(totalItems);
        this.setTotalAmount(totalAmount);
        this.setStartAt(startAt);
    }

    public void addItem(Product product, Quantity quantity) {
        var shoppingCartItem = ShoppingCartItem.create(this.id, product, quantity);
        this.items.add(shoppingCartItem);
        this.recalculateTotals();
    }

    public void removeItem(ShoppingCartItemId itemId) {
        var shoppingCartItem = this.findItem(itemId);
        this.items.remove(shoppingCartItem);
        this.recalculateTotals();
    }

    public void changeItemQuantity(ShoppingCartItemId itemId, Quantity quantity) {
        var shoppingCartItem = this.findItem(itemId);
        shoppingCartItem.changeQuantity(quantity);
        this.recalculateTotals();
    }

    public void clear() {
        this.items.clear();
        this.recalculateTotals();
    }

    public void refreshItem(Product product) {
        var shoppingCartItem = this.findItem(product.id());
        shoppingCartItem.refresh(product);
        recalculateTotals();
    }

    public boolean isEmpty() {
        return this.items.isEmpty();
    }

    private ShoppingCartItem findItem(ProductId productId) {
        return this.items.stream()
                .filter(item -> item.product().id().equals(productId))
                .findFirst()
                .orElseThrow(() -> new ShoppingCartProductItemNotFoundException(""));
    }

    private ShoppingCartItem findItem(ShoppingCartItemId itemId) {
        return this.items.stream()
                .filter(item -> item.id().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new ShoppingCartItemNotFoundException(""));
    }

    private void recalculateTotals() {
        var totalItemsQuantity = this.items.stream().map(item -> item.quantity().value())
                .reduce(0, Integer::sum);

        var totalItemsAmount = this.items.stream().map(item -> item.totalAmount().value())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        this.setTotalItems(new Quantity(totalItemsQuantity));
        this.setTotalAmount(new Money(totalItemsAmount));
    }

    @Override
    public ShoppingCartId id() {
        return id;
    }

    private void setId(ShoppingCartId id) {
        FieldValidator.requiresNonNull("id", id);
        this.id = id;
    }

    public Set<ShoppingCartItem> items() {
        return Collections.unmodifiableSet(items);
    }

    private void setItems(Set<ShoppingCartItem> items) {
        FieldValidator.requiresNonNull("items", items);
        this.items = items;
    }

    public Quantity totalItems() {
        return totalItems;
    }

    private void setTotalItems(Quantity totalItems) {
        FieldValidator.requiresNonNull("totalItems", totalItems);
        this.totalItems = totalItems;
    }

    public Money totalAmount() {
        return totalAmount;
    }

    private void setTotalAmount(Money totalAmount) {
        FieldValidator.requiresNonNull("totalAmount", totalAmount);
        this.totalAmount = totalAmount;
    }

    public LocalDate startAt() {
        return startAt;
    }

    private void setStartAt(LocalDate startAt) {
        FieldValidator.requiresNonNull("startAt", startAt);
        this.startAt = startAt;
    }
}
