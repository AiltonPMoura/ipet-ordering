package br.com.ipet.ordering.domain.model.shoppingcart;

import br.com.ipet.ordering.domain.model.AbstractEventSourceEntity;
import br.com.ipet.ordering.domain.model.AggregateRoot;
import br.com.ipet.ordering.domain.model.FieldValidator;
import br.com.ipet.ordering.domain.model.commons.valueobject.Money;
import br.com.ipet.ordering.domain.model.company.CompanyId;
import br.com.ipet.ordering.domain.model.product.Product;
import br.com.ipet.ordering.domain.model.commons.valueobject.Quantity;
import br.com.ipet.ordering.domain.model.customer.CustomerId;
import br.com.ipet.ordering.domain.model.product.ProductDoesNotBelongsToTheCompany;
import br.com.ipet.ordering.domain.model.product.ProductId;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class ShoppingCart
        extends AbstractEventSourceEntity
        implements AggregateRoot<ShoppingCartId> {
    private ShoppingCartId id;
    private CustomerId customerId;
    private CompanyId companyId;
    private Set<ShoppingCartItem> items;
    private Quantity totalItems;
    private Money totalAmount;
    private OffsetDateTime createdAt;

    static ShoppingCart startShopping(CustomerId customerId, CompanyId companyId) {
        var shoppingCart = new ShoppingCart(new ShoppingCartId(), customerId, companyId,
                new HashSet<>(), Quantity.ZERO, Money.ZERO, OffsetDateTime.now());

        shoppingCart.publishDomainEvent(new ShoppingCartCreatedEvent(
                shoppingCart.id, customerId, companyId, shoppingCart.createdAt));

        return shoppingCart;
    }

    @Builder(builderClassName = "ExistingShoppingCartBuilder", builderMethodName = "existing")
    private ShoppingCart(ShoppingCartId id, CustomerId customerId, CompanyId companyId, Set<ShoppingCartItem> items,
                         Quantity totalItems, Money totalAmount, OffsetDateTime createdAt) {
        this.setId(id);
        this.setCustomerId(customerId);
        this.setCompanyId(companyId);
        this.setItems(items);
        this.setTotalItems(totalItems);
        this.setTotalAmount(totalAmount);
        this.setCreatedAt(createdAt);
    }

    public void addItem(Product product, Quantity quantity, CustomerId customerId) {
        this.verifyIfShoppingCartBelongToTheCustomer(customerId);
        this.verifyProductBelongsToTheCompany(product);

        var shoppingCartItem = ShoppingCartItem.create(this.id, product, quantity);

        this.searchItemByProduct(product.id())
                .ifPresentOrElse(item -> updateItem(item, product, quantity),
                        () -> insertItem(shoppingCartItem));

        this.recalculateTotals();
        this.publishDomainEvent(new ShoppingCartItemAddedEvent(
                this.id, this.customerId, this.companyId, product.id(), OffsetDateTime.now()));
    }

    public void removeItem(ShoppingCartItemId itemId, CustomerId customerId) {
        this.verifyIfShoppingCartBelongToTheCustomer(customerId);
        var shoppingCartItem = this.findItem(itemId);
        this.items.remove(shoppingCartItem);
        this.recalculateTotals();
        this.publishDomainEvent(new ShoppingCartItemRemovedEvent(
                this.id, this.customerId, this.companyId, shoppingCartItem.product().id(), OffsetDateTime.now()));
    }

    public void changeItemQuantity(ShoppingCartItemId itemId, Quantity quantity, CustomerId customerId) {
        this.verifyIfShoppingCartBelongToTheCustomer(customerId);
        var shoppingCartItem = this.findItem(itemId);
        shoppingCartItem.changeQuantity(quantity);
        this.recalculateTotals();
    }

    public void empty(CustomerId customerId) {
        this.verifyIfShoppingCartBelongToTheCustomer(customerId);
        this.items.clear();
        this.totalAmount = Money.ZERO;
        this.totalItems = Quantity.ZERO;
        this.publishDomainEvent(new ShoppingCartEmpitiedEvent(
                this.id, this.customerId, this.companyId, OffsetDateTime.now()));
    }

    public void refreshItem(Product product) {
        FieldValidator.requiresNonNull("product", product);

        var shoppingCartItem = this.findItem(product.id());
        shoppingCartItem.refresh(product);
        recalculateTotals();
    }

    public boolean isEmpty() {
        return this.items.isEmpty();
    }

    public void discard(CustomerId customerId) {
        this.verifyIfShoppingCartBelongToTheCustomer(customerId);
        this.publishDomainEvent(new ShoppingCartDiscartedEvent(
                this.id, this.customerId, this.companyId, OffsetDateTime.now()));
    }

    private ShoppingCartItem findItem(ProductId productId) {
        FieldValidator.requiresNonNull("productId", productId);

        return this.items.stream()
                .filter(item -> item.product().id().equals(productId))
                .findFirst()
                .orElseThrow(() -> new ShoppingCartProductItemNotFoundException(""));
    }

    private ShoppingCartItem findItem(ShoppingCartItemId itemId) {
        FieldValidator.requiresNonNull("itemId", itemId);

        return this.items.stream()
                .filter(item -> item.id().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new ShoppingCartItemNotFoundException(""));
    }

    private void insertItem(ShoppingCartItem shoppingCartItem) {
        this.items.add(shoppingCartItem);
    }

    private void updateItem(ShoppingCartItem shoppingCartItem, Product product, Quantity quantity) {
        shoppingCartItem.refresh(product);
        shoppingCartItem.changeQuantity(quantity);
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

    private void verifyIfShoppingCartBelongToTheCustomer(CustomerId customerId) {
        if (!this.customerId.equals(customerId))
            throw new ShoppingCartDoesNotBelongToTheCustomer("");
    }

    private void verifyProductBelongsToTheCompany(Product product) {
        if (!this.companyId.equals(product.companyId()))
            throw new ProductDoesNotBelongsToTheCompany("");
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

    public CompanyId companyId() {
        return companyId;
    }

    private void setCompanyId(CompanyId companyId) {
        FieldValidator.requiresNonNull("companyId", companyId);
        this.companyId = companyId;
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
