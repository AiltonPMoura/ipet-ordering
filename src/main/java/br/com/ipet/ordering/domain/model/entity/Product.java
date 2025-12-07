package br.com.ipet.ordering.domain.model.entity;

import br.com.ipet.ordering.domain.model.util.FieldValidator;
import br.com.ipet.ordering.domain.model.valueobject.CompanyId;
import br.com.ipet.ordering.domain.model.valueobject.Money;
import br.com.ipet.ordering.domain.model.valueobject.ProductDescription;
import br.com.ipet.ordering.domain.model.valueobject.ProductId;
import br.com.ipet.ordering.domain.model.valueobject.ProductName;
import lombok.Builder;

public class Product {
    private ProductId id;
    private CompanyId companyId;
    private ProductName name;
    private ProductDescription description;
    private Money price;

    @Builder(builderClassName = "CreateNewProductBuilder", builderMethodName = "createNew")
    private static Product create(CompanyId companyId, ProductName name, ProductDescription description, Money price) {
        return new Product(new ProductId(), companyId, name, description, price);
    }

    @Builder(builderClassName = "CreateExistingProductBuilder", builderMethodName = "existing")
    private Product(ProductId id, CompanyId companyId, ProductName name, ProductDescription description, Money price) {
        this.setId(id);
        this.setCompanyId(companyId);
        this.setProductName(name);
        this.setDescription(description);
        this.setPrice(price);
    }

    void changeName(ProductName name) {
        this.setProductName(name);
    }

    void changeDescription(ProductDescription description) {
        FieldValidator.requiresNonNull("product description", description);
        this.setDescription(description);
    }

    void changePrice(Money price) {
        this.setPrice(price);
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

    private void setId(ProductId id) {
        FieldValidator.requiresNonNull("productId", id);
        this.id = id;
    }

    public void setCompanyId(CompanyId companyId) {
        FieldValidator.requiresNonNull("companyId", companyId);
        this.companyId = companyId;
    }

    private void setProductName(ProductName name) {
        FieldValidator.requiresNonNull("product name", name);
        this.name = name;
    }

    private void setDescription(ProductDescription description) {
        FieldValidator.requiresNonNull("product description", price);
        this.description = description;
    }

    private void setPrice(Money price) {
        FieldValidator.requiresNonNull("product price", price);
        this.price = price;
    }
}
