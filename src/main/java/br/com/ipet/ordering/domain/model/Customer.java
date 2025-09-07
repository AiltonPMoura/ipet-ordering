package br.com.ipet.ordering.domain.model;

import br.com.ipet.ordering.domain.util.FieldValidator;
import br.com.ipet.ordering.domain.valueobject.*;
import lombok.Builder;
import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
public class Customer {
    private CustumerId id;
    private FullName fullName;
    private Email email;
    private CelPhone celPhone;
    private Document document;
    private Address address;
    private OffsetDateTime registerAt;

    @Builder(builderClassName = "CreateNewCustomerBuild", builderMethodName = "createNew")
    private Customer(FullName fullName, Email email, CelPhone celPhone, Document document, Address address) {
        this.setId(new CustumerId());
        this.setFullName(fullName);
        this.setEmail(email);
        this.setCelPhone(celPhone);
        this.setDocument(document);
        this.setAddress(address);
        this.setRegisterAt(OffsetDateTime.now());
    }

    @Builder(builderClassName = "CreateExistingCustomerBuild", builderMethodName = "createExisting")
    private Customer(CustumerId id, FullName fullName, Email email, CelPhone celPhone, Document document,
                     Address address, OffsetDateTime registerAt) {
        this.setId(id);
        this.setFullName(fullName);
        this.setEmail(email);
        this.setCelPhone(celPhone);
        this.setDocument(document);
        this.setAddress(address);
        this.setRegisterAt(registerAt);
    }

    public void changeFullName(FullName fullName) {
        this.setFullName(fullName);
    }

    public void changeEmail(Email email) {
        this.setEmail(email);
    }

    public void changeCelPhone(CelPhone celPhone) {
        this.setCelPhone(celPhone);
    }

    public void changeDocument(Document document) {
        this.setDocument(document);
    }

    public void changeAddress(Address address) {
        this.setAddress(address);
    }

    private void setId(CustumerId id) {
        FieldValidator.notNull("id", id);
        this.id = id;
    }

    private void setFullName(FullName fullName) {
        FieldValidator.notNull("fullName", fullName);
        this.fullName = fullName;
    }

    private void setEmail(Email email) {
        FieldValidator.notNull("email", email);
        this.email = email;
    }

    private void setCelPhone(CelPhone celPhone) {
        FieldValidator.notNull("celPhone", celPhone);
        this.celPhone = celPhone;
    }

    private void setDocument(Document document) {
        FieldValidator.notNull("document", document);
        this.document = document;
    }

    private void setAddress(Address address) {
        FieldValidator.notNull("address", address);
        this.address = address;
    }

    private void setRegisterAt(OffsetDateTime registerAt) {
        FieldValidator.notNull("registerAt", registerAt);
        this.registerAt = registerAt;
    }
}
