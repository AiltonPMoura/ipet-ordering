package br.com.ipet.ordering.domain.model;

import br.com.ipet.ordering.domain.exception.ProductNotFoundException;
import br.com.ipet.ordering.domain.exception.ServiceNotFoundException;
import br.com.ipet.ordering.domain.util.FieldValidator;
import br.com.ipet.ordering.domain.valueobject.*;
import lombok.Builder;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Company {
    private CompanyId id;
    private CompanyName companyName;
    private Cnpj cnpj;
    private CelPhone celPhone;
    private Email email;
    private Address address;
    private Set<Service> services;
    private Set<Product> products;

    @Builder(builderClassName = "CreateNewCompanyBuilder", builderMethodName = "createNew")
    private static Company create(CompanyName companyName, Cnpj cnpj,
                                  CelPhone celPhone, Email email, Address address) {
        return new Company(new CompanyId(), companyName, cnpj, celPhone, email, address,
                new HashSet<>(), new HashSet<>());
    }

    @Builder(builderClassName = "CreateExistingCompanyBuilder", builderMethodName = "existing")
    private Company(CompanyId id, CompanyName companyName, Cnpj cnpj,
                   CelPhone celPhone, Email email, Address address,
                   Set<Service> services, Set<Product> products) {
        this.setId(id);
        this.setCompanyName(companyName);
        this.setCnpj(cnpj);
        this.setCelPhone(celPhone);
        this.setEmail(email);
        this.setAddress(address);
        this.setServices(services);
        this.setProducts(products);
    }

    public void addService(Name name, Description description, Money price) {
        var service = Service.createNew()
                .companyId(this.id)
                .name(name)
                .description(description)
                .price(price)
                .build();

        this.services.add(service);
    }

    public void addProduct(Name name, Description description, Money price) {
        var product = Product.createNew()
                .companyId(this.id)
                .name(name)
                .description(description)
                .price(price)
                .build();

        this.products.add(product);
    }

    public void changeCompanyName(CompanyName companyName) {
        this.setCompanyName(companyName);
    }

    public void changeCnpj(Cnpj cnpj) {
        this.setCnpj(cnpj);
    }

    public void changeCelPhone(CelPhone celPhone) {
        this.setCelPhone(celPhone);
    }

    public void changeEmail(Email email) {
        this.setEmail(email);
    }

    public void changeAddress(Address address) {
        this.setAddress(address);
    }

    public void changeServiceName(ServiceId id, Name name) {
        var service = findService(id);
        service.changeName(name);
    }

    public void changeServiceDescription(ServiceId id, Description description) {
        var service = findService(id);
        service.changeDescription(description);
    }

    public void changeServicePrice(ServiceId id, Money price) {
        var service = findService(id);
        service.changePrice(price);
    }

    public void changeProductName(ProductId id, Name name) {
        var product = findProduct(id);
        product.changeName(name);
    }

    public void changeProductDescription(ProductId id, Description name) {
        var product = findProduct(id);
        product.changeDescription(name);
    }

    public void changeProductPrice(ProductId id, Money price) {
        var product = findProduct(id);
        product.changePrice(price);
    }

    public CompanyId id() {
        return id;
    }

    public CompanyName companyName() {
        return companyName;
    }

    public Cnpj cnpj() {
        return cnpj;
    }

    public CelPhone celPhone() {
        return celPhone;
    }

    public Email email() {
        return email;
    }

    public Address address() {
        return address;
    }

    private Service findService(ServiceId id) {
        return this.services.stream()
                .filter(service -> service.id().equals(id))
                .findFirst()
                .orElseThrow(() -> new ServiceNotFoundException(id.value().toString(), this.id.value().toString()));
    }

    private Product findProduct(ProductId id) {
        return this.products.stream()
                .filter(product -> product.id().equals(id))
                .findFirst()
                .orElseThrow(() -> new ProductNotFoundException(id.value().toString(), this.id.value().toString()));
    }

    private void setId(CompanyId id) {
        this.id = id;
    }

    public Set<Service> services() {
        return Collections.unmodifiableSet(services);
    }

    public Set<Product> products() {
        return Collections.unmodifiableSet(products);
    }

    private void setProducts(Set<Product> products) {
        this.products = products;
    }

    private void setServices(Set<Service> services) {
        this.services = services;
    }

    private void setAddress(Address address) {
        FieldValidator.requiresNonNull("addres", address);
        this.address = address;
    }

    private void setEmail(Email email) {
        FieldValidator.requiresNonNull("email", email);
        this.email = email;
    }

    private void setCelPhone(CelPhone celPhone) {
        FieldValidator.requiresNonNull("celPhone", celPhone);
        this.celPhone = celPhone;
    }

    private void setCnpj(Cnpj cnpj) {
        FieldValidator.requiresNonNull("cnpj", cnpj);
        this.cnpj = cnpj;
    }

    private void setCompanyName(CompanyName companyName) {
        FieldValidator.requiresNonNull("company name", companyName);
        this.companyName = companyName;
    }

}
