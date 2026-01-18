package br.com.ipet.ordering.domain.model.entity;

import br.com.ipet.ordering.domain.model.util.FieldValidator;
import br.com.ipet.ordering.domain.model.valueobject.CompanyId;
import br.com.ipet.ordering.domain.model.valueobject.Money;
import br.com.ipet.ordering.domain.model.valueobject.ProductDescription;
import br.com.ipet.ordering.domain.model.valueobject.ProductId;
import br.com.ipet.ordering.domain.model.valueobject.ProductName;
import br.com.ipet.ordering.domain.model.valueobject.Quantity;
import lombok.Builder;

public class Product {
    private ProductId id;
    private CompanyId companyId;
    private ProductName name;
    private ProductDescription description;
    private Money price;
    private Quantity totalStock;

    @Builder(builderClassName = "CreateNewProductBuilder", builderMethodName = "createNew")
    private static Product create(CompanyId companyId,
                                  ProductName name, ProductDescription description,
                                  Money price, Quantity totalStock) {
        return new Product(new ProductId(), companyId, name, description, price, totalStock);
    }

    @Builder(builderClassName = "CreateExistingProductBuilder", builderMethodName = "existing")
    private Product(ProductId id, CompanyId companyId,
                    ProductName name, ProductDescription description,
                    Money price, Quantity stock) {
        this.setId(id);
        this.setCompanyId(companyId);
        this.setName(name);
        this.setDescription(description);
        this.setPrice(price);
        this.setTotalStock(totalStock);
    }

    void changeName(ProductName name) {
        this.setName(name);
    }

    void changeDescription(ProductDescription description) {
        FieldValidator.requiresNonNull("product description", description);
        this.setDescription(description);
    }

    void changePrice(Money price) {
        this.setPrice(price);
    }

    void addTotalStock(Quantity quantity) {
        FieldValidator.requiresNonNull("quantity", quantity);
        this.setTotalStock(this.totalStock.sum(quantity));
    }

    void subtractTotalStock(Quantity quantity) {
        FieldValidator.requiresNonNull("quantity", quantity);

        if (quantity.value() > this.totalStock.value())
            throw new RuntimeException();

        this.setTotalStock(this.totalStock.subtract(quantity));
    }

    public boolean availableStock() {
        return this.totalStock.value() > 0;
    }

    public ProductId id() {
        return id;
    }

    public CompanyId companyId() {
        return companyId;
    }

    public ProductName name() {
        return name;
    }

    public ProductDescription description() {
        return description;
    }

    public Money price() {
        return price;
    }

    public Quantity totalStock() {
        return totalStock;
    }

    private void setId(ProductId id) {
        FieldValidator.requiresNonNull("productId", id);
        this.id = id;
    }

    public void setCompanyId(CompanyId companyId) {
        FieldValidator.requiresNonNull("companyId", companyId);
        this.companyId = companyId;
    }

    private void setName(ProductName name) {
        FieldValidator.requiresNonNull("product name", name);
        this.name = name;
    }

    private void setDescription(ProductDescription description) {
        this.description = description;
    }

    private void setPrice(Money price) {
        FieldValidator.requiresNonNull("product price", price);
        this.price = price;
    }

    private void setTotalStock(Quantity totalStock) {
        this.totalStock = totalStock;
    }
}
