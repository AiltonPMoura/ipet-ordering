package br.com.ipet.ordering.domain.model.customer;

import br.com.ipet.ordering.domain.model.AbstractEventSourceEntity;
import br.com.ipet.ordering.domain.model.AggregateRoot;
import br.com.ipet.ordering.domain.model.FieldValidator;
import br.com.ipet.ordering.domain.model.commons.document.Document;
import br.com.ipet.ordering.domain.model.commons.valueobject.Address;
import br.com.ipet.ordering.domain.model.commons.valueobject.Phone;
import br.com.ipet.ordering.domain.model.commons.valueobject.Email;
import br.com.ipet.ordering.domain.model.commons.valueobject.FullName;
import lombok.AccessLevel;
import lombok.Builder;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Customer
        extends AbstractEventSourceEntity
        implements AggregateRoot<CustomerId> {
    private CustomerId id;
    private FullName fullName;
    private Email email;
    private Phone phone;
    private Document document;
    private BirthDate birthDate;
    private Set<CustomerAddress> address;
    private OffsetDateTime registerAt;

    @Builder(builderClassName = "CreateNewCustomerBuilder", builderMethodName = "createNew", access = AccessLevel.PACKAGE)
    private static Customer create(FullName fullName, Email email, Phone phone,
                                   Document document, BirthDate birthDate, Address address) {

        var customer = new Customer(new CustomerId(), fullName, email,
                phone, document, birthDate, new HashSet<>(), OffsetDateTime.now());

        var customerAddress = CustomerAddress.create(customer.id, address, true);

        customer.address.add(customerAddress);

        customer.publishDomainEvent(new CustomerRegisteredEvent(customer.id, customer.fullName, customer.email, customer.registerAt));

        return customer;
    }

    @Builder(builderClassName = "CreateExistingCustomerBuilder", builderMethodName = "existing")
    private Customer(CustomerId id, FullName fullName, Email email, Phone phone, Document document,
                     BirthDate birthDate, Set<CustomerAddress> address, OffsetDateTime registerAt) {
        this.setId(id);
        this.setFullName(fullName);
        this.setEmail(email);
        this.setPhone(phone);
        this.setDocument(document);
        this.setBirthDate(birthDate);
        this.setAddress(address);
        this.setRegisterAt(registerAt);
    }

    public OffsetDateTime registerAt() {
        return registerAt;
    }

    public CustomerAddressId addAddress(Address address) {
        FieldValidator.requiresNonNull("address", address);

        var customerAddress = CustomerAddress.create(this.id, address, false);

        this.address.add(customerAddress);

        return customerAddress.id();
    }

    public void removeAddress(CustomerAddressId addressId) {
        var customerAddress = this.findCustomerAddress(addressId);

        if (customerAddress.isPrincipal())
            throw new CannotDeletePrincipalAddress("");

        this.address.remove(customerAddress);
    }

    public void changeAddress(CustomerAddressId addressId, Address address) {
        var customerAddress = this.findCustomerAddress(addressId);
        customerAddress.changeAddress(address);
    }

    public void changePrincipalAddress(CustomerAddressId addressId) {
        var customerAddress = this.findCustomerAddress(addressId);
        this.disableCurrentPrincipalAddress();
        customerAddress.changePrincipal(true);
    }

    public Address principalAddress() {
        return this.address.stream()
                .filter(CustomerAddress::isPrincipal)
                .findFirst()
                .map(CustomerAddress::address)
                .orElseThrow(() -> new CustomerDoesNotContainPrincipalAddressException(""));
    }

    public Address findAddress(CustomerAddressId customerAddressId) {
        return this.findCustomerAddress(customerAddressId).address();
    }

    public void changeName(FullName fullName) {
        this.setFullName(fullName);
    }

    public void changeEmail(Email email) {
        this.setEmail(email);
    }

    public void changePhone(Phone phone) {
        this.setPhone(phone);
    }

    public void changeDocument(Document document) {
        this.setDocument(document);
    }

    public void changeBirthDate(BirthDate birthDate) {
        this.setBirthDate(birthDate);
    }

    private CustomerAddress findCustomerAddress(CustomerAddressId customerAddressId) {
        FieldValidator.requiresNonNull("addressId", customerAddressId);

        return this.address.stream()
                .filter(customerAddress -> customerAddress.id().equals(customerAddressId))
                .findFirst()
                .orElseThrow(() -> new CustomerAddressNotFoundException(""));
    }

    private void disableCurrentPrincipalAddress() {
        this.address.stream()
                .filter(CustomerAddress::isPrincipal)
                .findFirst()
                .ifPresent(principalAddress -> principalAddress.changePrincipal(false));
    }

    public CustomerId id() {
        return id;
    }

    private void setId(CustomerId id) {
        FieldValidator.requiresNonNull("id", id);
        this.id = id;
    }

    public FullName fullName() {
        return fullName;
    }

    private void setFullName(FullName fullName) {
        FieldValidator.requiresNonNull("fullName", fullName);
        this.fullName = fullName;
    }

    public Email email() {
        return email;
    }

    private void setEmail(Email email) {
        FieldValidator.requiresNonNull("email", email);
        this.email = email;
    }

    public Phone phone() {
        return phone;
    }

    private void setPhone(Phone phone) {
        FieldValidator.requiresNonNull("phone", phone);
        this.phone = phone;
    }

    public Document document() {
        return document;
    }

    private void setDocument(Document document) {
        FieldValidator.requiresNonNull("document", document);
        this.document = document;
    }

    public BirthDate birthDate() {
        return birthDate;
    }

    private void setBirthDate(BirthDate birthDate) {
        FieldValidator.requiresNonNull("birthDate", birthDate);
        this.birthDate = birthDate;
    }

    public Set<CustomerAddress> customerAddresses() {
        return Collections.unmodifiableSet(address);
    }

    private void setAddress(Set<CustomerAddress> customerAddresses) {
        FieldValidator.requiresNonNull("customerAddresses", customerAddresses);
        this.address = customerAddresses;
    }

    private void setRegisterAt(OffsetDateTime registerAt) {
        FieldValidator.requiresNonNull("registerAt", registerAt);
        this.registerAt = registerAt;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Customer customer)) return false;
        return Objects.equals(id, customer.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
