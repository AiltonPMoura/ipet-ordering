package br.com.ipet.ordering.domain.model.customer;

import br.com.ipet.ordering.domain.model.AggregateRoot;
import br.com.ipet.ordering.domain.model.commons.Address;
import br.com.ipet.ordering.domain.model.commons.CelPhone;
import br.com.ipet.ordering.domain.model.commons.Email;
import br.com.ipet.ordering.domain.model.commons.FullName;
import br.com.ipet.ordering.domain.model.FieldValidator;
import lombok.Builder;

import java.time.OffsetDateTime;

public class Customer implements AggregateRoot<CustomerId> {
    private CustomerId id;
    private FullName fullName;
    private Email email;
    private CelPhone celPhone;
    private Cpf cpf;
    private Address address;
    private OffsetDateTime registerAt;

    @Builder(builderClassName = "CreateNewCustomerBuild", builderMethodName = "createNew")
    private static Customer create(FullName fullName, Email email, CelPhone celPhone, Cpf cpf, Address address) {
        return new Customer(new CustomerId(), fullName, email, celPhone, cpf, address, OffsetDateTime.now());
    }

    @Builder(builderClassName = "CreateExistingCustomerBuild", builderMethodName = "existing")
    private Customer(CustomerId id, FullName fullName, Email email,
                     CelPhone celPhone, Cpf cpf, Address address, OffsetDateTime registerAt) {
        this.setId(id);
        this.setFullName(fullName);
        this.setEmail(email);
        this.setCelPhone(celPhone);
        this.setCpf(cpf);
        this.setAddress(address);
        this.setRegisterAt(registerAt);
    }

    public void changeName(FullName fullName) {
        this.setFullName(fullName);
    }

    void changeEmail(Email email) {
        this.setEmail(email);
    }

    public void changeCelPhone(CelPhone celPhone) {
        this.setCelPhone(celPhone);
    }

    public void changeCpf(Cpf cpf) {
        this.setCpf(cpf);
    }

    public void changeAddress(Address address) {
        this.setAddress(address);
    }

    public CustomerId id() {
        return id;
    }

    public FullName fullName() {
        return fullName;
    }

    public Email email() {
        return email;
    }

    public CelPhone celPhone() {
        return celPhone;
    }

    public Cpf document() {
        return cpf;
    }

    public Address address() {
        return address;
    }

    public OffsetDateTime registerAt() {
        return registerAt;
    }

    private void setId(CustomerId id) {
        FieldValidator.requiresNonNull("customer id", id);
        this.id = id;
    }

    private void setFullName(FullName fullName) {
        FieldValidator.requiresNonNull("customer fullName", fullName);
        this.fullName = fullName;
    }

    private void setEmail(Email email) {
        FieldValidator.requiresNonNull("customer email", email);
        this.email = email;
    }

    private void setCelPhone(CelPhone celPhone) {
        FieldValidator.requiresNonNull("customer celPhone", celPhone);
        this.celPhone = celPhone;
    }

    private void setCpf(Cpf cpf) {
        FieldValidator.requiresNonNull("customer document", cpf);
        this.cpf = cpf;
    }

    private void setAddress(Address address) {
        FieldValidator.requiresNonNull("customer address", address);
        this.address = address;
    }

    private void setRegisterAt(OffsetDateTime registerAt) {
        FieldValidator.requiresNonNull("registerAt", registerAt);
        this.registerAt = registerAt;
    }
}
