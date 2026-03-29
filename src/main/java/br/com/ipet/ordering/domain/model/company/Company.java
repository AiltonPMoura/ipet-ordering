package br.com.ipet.ordering.domain.model.company;

import br.com.ipet.ordering.domain.model.AggregateRoot;
import br.com.ipet.ordering.domain.model.FieldValidator;
import br.com.ipet.ordering.domain.model.commons.valueobject.Address;
import br.com.ipet.ordering.domain.model.commons.valueobject.CelPhone;
import br.com.ipet.ordering.domain.model.commons.valueobject.Email;
import lombok.Builder;

import java.time.OffsetDateTime;

public class Company implements AggregateRoot<CompanyId> {
    private CompanyId id;
    private CompanyName name;
    private Cnpj cnpj;
    private CelPhone celPhone;
    private Email email;
    private Address address;
    private OffsetDateTime registeredAt;

    @Builder(builderClassName = "CreateNewCompanyBuilder", builderMethodName = "createNew")
    private static Company create(CompanyName name, Cnpj cnpj,
                                  CelPhone celPhone, Email email, Address address) {
        return new Company(new CompanyId(), name, cnpj,
                celPhone, email, address, OffsetDateTime.now());
    }

    @Builder(builderClassName = "CreateExistingCompanyBuilder", builderMethodName = "existing")
    private Company(CompanyId id, CompanyName name, Cnpj cnpj,
                    CelPhone celPhone, Email email, Address address, OffsetDateTime registeredAt) {
        this.setId(id);
        this.setName(name);
        this.setCnpj(cnpj);
        this.setCelPhone(celPhone);
        this.setEmail(email);
        this.setAddress(address);
        this.setRegisteredAt(registeredAt);
    }

    /*public void addService(ServiceName name, ServiceDescription description, Money price) {
        var service = Service.createNew()
                .companyId(this.id)
                .name(name)
                .description(description)
                .price(price)
                .build();

        this.services.add(service);
    }

    public void addProduct(ProductName name, ProductDescription description, Money price) {
        var product = Product.createNew()
                .companyId(this.id)
                .name(name)
                .description(description)
                .price(price)
                .build();

        this.products.add(product);
    }*/

    public void changeCompanyName(CompanyName companyName) {
        this.setName(companyName);
    }

    void changeCnpj(Cnpj cnpj) {
        this.setCnpj(cnpj);
    }

    public void changeCelPhone(CelPhone celPhone) {
        this.setCelPhone(celPhone);
    }

    void changeEmail(Email email) {
        this.setEmail(email);
    }

    public void changeAddress(Address address) {
        this.setAddress(address);
    }

    /*public void changeServiceName(ServiceId id, ServiceName name) {
        var service = findService(id);
        service.changeName(name);
    }

    public void changeServiceDescription(ServiceId id, ServiceDescription description) {
        var service = findService(id);
        service.changeDescription(description);
    }

    public void changeServicePrice(ServiceId id, Money price) {
        var service = findService(id);
        service.changePrice(price);
    }

    public void changeProductName(ProductId id, ProductName name) {
        var product = findProduct(id);
        product.changeName(name);
    }

    public void changeProductDescription(ProductId id, ProductDescription description) {
        var product = findProduct(id);
        product.changeDescription(description);
    }

    public void changeProductPrice(ProductId id, Money price) {
        var product = findProduct(id);
        product.changePrice(price);
    }*/

    public CompanyId id() {
        return id;
    }

    public CompanyName name() {
        return name;
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

    public OffsetDateTime registeredAt() {
        return registeredAt;
    }

    /*private Service findService(ServiceId id) {
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
    }*/

    private void setId(CompanyId id) {
        this.id = id;
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

    private void setName(CompanyName name) {
        FieldValidator.requiresNonNull("company name", name);
        this.name = name;
    }

    private void setRegisteredAt(OffsetDateTime registeredAt) {
        FieldValidator.requiresNonNull("registeredAt", registeredAt);
        this.registeredAt = registeredAt;
    }

}
