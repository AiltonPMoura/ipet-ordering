package br.com.ipet.ordering.domain.model.customer;

import br.com.ipet.ordering.domain.model.AbstractEventSourceEntity;
import br.com.ipet.ordering.domain.model.AggregateRoot;
import br.com.ipet.ordering.domain.model.FieldValidator;
import br.com.ipet.ordering.domain.model.commons.document.Document;
import br.com.ipet.ordering.domain.model.commons.valueobject.Address;
import br.com.ipet.ordering.domain.model.commons.valueobject.Email;
import br.com.ipet.ordering.domain.model.commons.valueobject.FullName;
import br.com.ipet.ordering.domain.model.commons.valueobject.Phone;
import lombok.AccessLevel;
import lombok.Builder;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
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
    private Set<CustomerAddress> addresses;
    private OffsetDateTime registeredAt;

    @Builder(builderClassName = "CreateNewCustomerBuilder", builderMethodName = "createNew", access = AccessLevel.PACKAGE)
    private static Customer create(FullName fullName, Email email, Phone phone,
                                   Document document, BirthDate birthDate, Address address) {

        var customer = new Customer(new CustomerId(), fullName, email,
                phone, document, birthDate, new HashSet<>(), OffsetDateTime.now(ZoneOffset.UTC));

            var customerAddress = CustomerAddress.create(customer.id, address, true);
            customer.addresses.add(customerAddress);

        customer.publishDomainEvent(new CustomerRegisteredEvent(customer.id, customer.fullName, customer.email, customer.registeredAt));

        return customer;
    }

    @Builder(builderClassName = "CreateExistingCustomerBuilder", builderMethodName = "existing")
    private Customer(CustomerId id, FullName fullName, Email email, Phone phone, Document document,
                     BirthDate birthDate, Set<CustomerAddress> addresses, OffsetDateTime registeredAt) {
        this.setId(id);
        this.setFullName(fullName);
        this.setEmail(email);
        this.setPhone(phone);
        this.setDocument(document);
        this.setBirthDate(birthDate);
        this.setAddresses(addresses);
        this.setRegisteredAt(registeredAt);
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

    public CustomerAddressId addAddress(Address address) {
        FieldValidator.requiresNonNull("address", address);
        var customerAddress = CustomerAddress.create(this.id, address, false);
        this.addresses.add(customerAddress);
        return customerAddress.id();
    }

    public void removeAddress(CustomerAddressId addressId) {
        var customerAddress = this.findCustomerAddress(addressId);
        if (customerAddress.isPrincipal()) throw new CannotDeletePrincipalAddress("");
        this.addresses.remove(customerAddress);
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
        return this.addresses.stream()
                .filter(CustomerAddress::isPrincipal)
                .findFirst()
                .map(CustomerAddress::address)
                .orElseThrow(() -> new CustomerDoesNotContainPrincipalAddressException(""));
    }

    public Address findAddress(CustomerAddressId customerAddressId) {
        return this.findCustomerAddress(customerAddressId).address();
    }

    private CustomerAddress findCustomerAddress(CustomerAddressId customerAddressId) {
        FieldValidator.requiresNonNull("addressId", customerAddressId);

        return this.addresses.stream()
                .filter(customerAddress -> customerAddress.id().equals(customerAddressId))
                .findFirst()
                .orElseThrow(() -> new CustomerAddressNotFoundException(""));
    }

    private void disableCurrentPrincipalAddress() {
        this.addresses.stream()
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
        return Collections.unmodifiableSet(addresses);
    }

    private void setAddresses(Set<CustomerAddress> customerAddresses) {
        FieldValidator.requiresNonNull("customerAddresses", customerAddresses);
        this.addresses = customerAddresses;
    }

    public OffsetDateTime registeredAt() {
        return registeredAt;
    }

    private void setRegisteredAt(OffsetDateTime registeredAt) {
        FieldValidator.requiresNonNull("registeredAt", registeredAt);
        this.registeredAt = registeredAt;
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
