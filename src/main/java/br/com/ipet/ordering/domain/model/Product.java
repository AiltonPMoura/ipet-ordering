package br.com.ipet.ordering.domain.model;

import br.com.ipet.ordering.domain.util.FieldValidator;
import br.com.ipet.ordering.domain.valueobject.*;
import lombok.Builder;

public class Product {
    private ProductId id;
    private CompanyId companyId;
    private Name name;
    private Description description;
    private Money price;

    @Builder(builderClassName = "CreateNewProductBuilder", builderMethodName = "createNew")
    private static Product create(CompanyId companyId, Name name, Description description, Money price) {
        return new Product(new ProductId(), companyId, name, description, price);
    }

    @Builder(builderClassName = "CreateExistingProductBuilder", builderMethodName = "existing")
    private Product(ProductId id, CompanyId companyId, Name name, Description description, Money price) {
        this.setId(id);
        this.setCompanyId(companyId);
        this.setName(name);
        this.setDescription(description);
        this.setPrice(price);
    }

    void changeName(Name name) {
        this.setName(name);
    }

    void changeDescription(Description description) {
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

    public Name name() {
        return name;
    }

    public Description description() {
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

    private void setName(Name name) {
        FieldValidator.requiresNonNull("product name", name);
        this.name = name;
    }

    private void setDescription(Description description) {
        this.description = description;
    }

    private void setPrice(Money price) {
        FieldValidator.requiresNonNull("product price", price);
        this.price = price;
    }
}
