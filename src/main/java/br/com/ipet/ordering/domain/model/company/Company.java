package br.com.ipet.ordering.domain.model.company;

import br.com.ipet.ordering.domain.model.AggregateRoot;
import br.com.ipet.ordering.domain.model.FieldValidator;
import br.com.ipet.ordering.domain.model.commons.document.Document;
import br.com.ipet.ordering.domain.model.commons.valueobject.Address;
import br.com.ipet.ordering.domain.model.commons.valueobject.Phone;
import br.com.ipet.ordering.domain.model.commons.valueobject.Email;
import lombok.Builder;

import java.time.OffsetDateTime;

public class Company implements AggregateRoot<CompanyId> {
    private CompanyId id;
    private CompanyName name;
    private Document document;
    private Phone phone;
    private Email email;
    private Address address;
    private OffsetDateTime registeredAt;

    @Builder(builderClassName = "CreateNewCompanyBuilder", builderMethodName = "createNew")
    private static Company create(CompanyName name, Document document,
                                  Phone phone, Email email, Address address) {
        return new Company(new CompanyId(), name, document,
                phone, email, address, OffsetDateTime.now());
    }

    @Builder(builderClassName = "CreateExistingCompanyBuilder", builderMethodName = "existing")
    private Company(CompanyId id, CompanyName name, Document document,
                    Phone phone, Email email, Address address, OffsetDateTime registeredAt) {
        this.setId(id);
        this.setName(name);
        this.setDocument(document);
        this.setPhone(phone);
        this.setEmail(email);
        this.setAddress(address);
        this.setRegisteredAt(registeredAt);
    }

    public void changeCompanyName(CompanyName companyName) {
        this.setName(companyName);
    }

    public void changeDocument(Document document) {
        this.setDocument(document);
    }

    public void changePhone(Phone phone) {
        this.setPhone(phone);
    }

    void changeEmail(Email email) {
        this.setEmail(email);
    }

    public void changeAddress(Address address) {
        this.setAddress(address);
    }

    public CompanyId id() {
        return id;
    }

    private void setId(CompanyId id) {
        this.id = id;
    }

    public CompanyName name() {
        return name;
    }

    private void setName(CompanyName name) {
        FieldValidator.requiresNonNull("name", name);
        this.name = name;
    }

    public Document document() {
        return document;
    }

    private void setDocument(Document document) {
        FieldValidator.requiresNonNull("document", document);
        this.document = document;
    }

    public Phone phone() {
        return phone;
    }

    private void setPhone(Phone phone) {
        FieldValidator.requiresNonNull("phone", phone);
        this.phone = phone;
    }

    public Email email() {
        return email;
    }

    private void setEmail(Email email) {
        FieldValidator.requiresNonNull("email", email);
        this.email = email;
    }

    public Address address() {
        return address;
    }

    private void setAddress(Address address) {
        FieldValidator.requiresNonNull("addres", address);
        this.address = address;
    }

    public OffsetDateTime registeredAt() {
        return registeredAt;
    }

    private void setRegisteredAt(OffsetDateTime registeredAt) {
        FieldValidator.requiresNonNull("registeredAt", registeredAt);
        this.registeredAt = registeredAt;
    }

}
