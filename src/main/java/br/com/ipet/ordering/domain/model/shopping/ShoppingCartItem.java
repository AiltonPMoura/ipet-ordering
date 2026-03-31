package br.com.ipet.ordering.domain.model.shopping;

import br.com.ipet.ordering.domain.model.FieldValidator;
import br.com.ipet.ordering.domain.model.commons.exception.QuantityNeedsGreaterThanZeroException;
import br.com.ipet.ordering.domain.model.commons.valueobject.Money;
import br.com.ipet.ordering.domain.model.commons.valueobject.Product;
import br.com.ipet.ordering.domain.model.commons.valueobject.Quantity;
import lombok.Builder;

public class ShoppingCartItem {
    private ShoppingCartItemId id;
    private ShoppingCartId shoppingCartId;
    private Product product;
    private Quantity quantity;
    private Money totalAmount;

    static ShoppingCartItem create(ShoppingCartId shoppingCartId, Product product, Quantity quantity) {
        var shoppingCartItem = new ShoppingCartItem(new ShoppingCartItemId(), shoppingCartId,
                product, quantity, Money.ZERO);

        shoppingCartItem.recalculateTotals();

        return shoppingCartItem;
    }

    @Builder(builderClassName = "CreateShoppingCartBuilder", builderMethodName = "existing")
    private ShoppingCartItem(ShoppingCartItemId id, ShoppingCartId shoppingCartId,
                             Product product, Quantity quantity, Money totalAmount) {
        this.setId(id);
        this.setProduct(product);
        this.setQuantity(quantity);
        this.setTotalAmount(totalAmount);
    }

    void changeQuantity(Quantity quantity) {
        if (quantity.value() < 1)
            throw new QuantityNeedsGreaterThanZeroException();

        this.setQuantity(quantity);
        recalculateTotals();
    }

    void refresh(Product product) {
        this.setProduct(product);
        this.recalculateTotals();
    }

    private void recalculateTotals() {
        var total = this.product.price().multiply(quantity);
        this.setTotalAmount(total);
    }

    public ShoppingCartItemId id() {
        return id;
    }

    private void setId(ShoppingCartItemId id) {
        FieldValidator.requiresNonNull("id", id);
        this.id = id;
    }

    public Product product() {
        return product;
    }

    private void setProduct(Product product) {
        FieldValidator.requiresNonNull("product", product);
        this.product = product;
    }

    public Quantity quantity() {
        return quantity;
    }

    private void setQuantity(Quantity quantity) {
        FieldValidator.requiresNonNull("quantity", quantity);
        this.quantity = quantity;
    }

    public Money totalAmount() {
        return totalAmount;
    }

    private void setTotalAmount(Money totalAmount) {
        FieldValidator.requiresNonNull("totalAmount", totalAmount);
        this.totalAmount = totalAmount;
    }
}
