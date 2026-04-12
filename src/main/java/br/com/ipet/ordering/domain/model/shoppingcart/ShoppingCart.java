package br.com.ipet.ordering.domain.model.shoppingcart;

import br.com.ipet.ordering.domain.model.AggregateRoot;
import br.com.ipet.ordering.domain.model.FieldValidator;
import br.com.ipet.ordering.domain.model.commons.valueobject.Money;
import br.com.ipet.ordering.domain.model.product.Product;
import br.com.ipet.ordering.domain.model.commons.valueobject.Quantity;
import br.com.ipet.ordering.domain.model.customer.CustomerId;
import br.com.ipet.ordering.domain.model.product.ProductId;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class ShoppingCart implements AggregateRoot<ShoppingCartId> {

    private ShoppingCartId id;
    private CustomerId customerId;
    private Set<ShoppingCartItem> items;
    private Quantity totalItems;
    private Money totalAmount;
    private OffsetDateTime createdAt;

    public static ShoppingCart startShopping(CustomerId customerId) {
        return new ShoppingCart(new ShoppingCartId(), customerId,
                new HashSet<>(), Quantity.ZERO, Money.ZERO, OffsetDateTime.now());
    }

    @Builder(builderClassName = "ExistingShoppingCartBuilder", builderMethodName = "existing")
    private ShoppingCart(ShoppingCartId id, CustomerId customerId, Set<ShoppingCartItem> items,
                         Quantity totalItems, Money totalAmount, OffsetDateTime createdAt) {
        this.setId(id);
        this.setCustomerId(customerId);
        this.setItems(items);
        this.setTotalItems(totalItems);
        this.setTotalAmount(totalAmount);
        this.setCreatedAt(createdAt);
    }

    public void addItem(Product product, Quantity quantity) {
        var shoppingCartItem = ShoppingCartItem.create(this.id, product, quantity);

        this.searchItemByProduct(product.id())
                .ifPresentOrElse(item -> updateItem(item, product, quantity),
                        () -> insertItem(shoppingCartItem));

        this.recalculateTotals();
    }

    private void insertItem(ShoppingCartItem shoppingCartItem) {
        this.items.add(shoppingCartItem);
    }

    private void updateItem(ShoppingCartItem shoppingCartItem, Product product, Quantity quantity) {
        shoppingCartItem.refresh(product);
        shoppingCartItem.changeQuantity(quantity);
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

    public void empty() {
        this.items.clear();
        this.totalAmount = Money.ZERO;
        this.totalItems = Quantity.ZERO;
    }

    public void refreshItem(Product product) {
        FieldValidator.requiresNonNull("product", product);

        var shoppingCartItem = this.findItem(product.id());
        shoppingCartItem.refresh(product);
        recalculateTotals();
    }
    public ShoppingCartItem findItem(ProductId productId) {
        FieldValidator.requiresNonNull("productId", productId);

        return this.items.stream()
                .filter(item -> item.product().id().equals(productId))
                .findFirst()
                .orElseThrow(() -> new ShoppingCartProductItemNotFoundException(""));
    }

    public ShoppingCartItem findItem(ShoppingCartItemId itemId) {
        FieldValidator.requiresNonNull("itemId", itemId);

        return this.items.stream()
                .filter(item -> item.id().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new ShoppingCartItemNotFoundException(""));
    }

    public boolean isEmpty() {
        return this.items.isEmpty();
    }

    private void recalculateTotals() {
        var totalItemsQuantity = this.items.stream().map(item -> item.quantity().value())
                .reduce(0, Integer::sum);

        var totalItemsAmount = this.items.stream().map(item -> item.totalAmount().value())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        this.setTotalItems(new Quantity(totalItemsQuantity));
        this.setTotalAmount(new Money(totalItemsAmount));
    }

    private Optional<ShoppingCartItem> searchItemByProduct(ProductId productId) {
        return this.items.stream()
                .filter(item -> item.product().id().equals(productId))
                .findFirst();
    }

    @Override
    public ShoppingCartId id() {
        return id;
    }

    private void setId(ShoppingCartId id) {
        FieldValidator.requiresNonNull("id", id);
        this.id = id;
    }

    public CustomerId customerId() {
        return customerId;
    }

    private void setCustomerId(CustomerId customerId) {
        FieldValidator.requiresNonNull("customerId", customerId);
        this.customerId = customerId;
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

    public OffsetDateTime createdAt() {
        return createdAt;
    }

    private void setCreatedAt(OffsetDateTime createdAt) {
        FieldValidator.requiresNonNull("createdAt", createdAt);
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ShoppingCart that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
