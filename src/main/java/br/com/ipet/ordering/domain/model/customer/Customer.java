package br.com.ipet.ordering.domain.model.customer;

import br.com.ipet.ordering.domain.model.AggregateRoot;
import br.com.ipet.ordering.domain.model.FieldValidator;
import br.com.ipet.ordering.domain.model.commons.Document;
import br.com.ipet.ordering.domain.model.commons.valueobject.Address;
import br.com.ipet.ordering.domain.model.commons.valueobject.CelPhone;
import br.com.ipet.ordering.domain.model.commons.valueobject.Email;
import br.com.ipet.ordering.domain.model.commons.valueobject.FullName;
import lombok.Builder;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Customer implements AggregateRoot<CustomerId> {
    private CustomerId id;
    private FullName fullName;
    private Email email;
    private CelPhone celPhone;
    private Document document;
    private Set<CustomerAddress> address;
    private OffsetDateTime registerAt;

    @Builder(builderClassName = "CreateNewCustomerBuilder", builderMethodName = "createNew")
    private static Customer create(FullName fullName, Email email,
                                   CelPhone celPhone, Document document, Address address) {

        var customer = new Customer(new CustomerId(), fullName,
                email, celPhone, document, new HashSet<>(), OffsetDateTime.now());

        customer.addAddress(address, true);

        return customer;
    }

    @Builder(builderClassName = "CreateExistingCustomerBuilder", builderMethodName = "existing")
    private Customer(CustomerId id, FullName fullName, Email email,
                     CelPhone celPhone, Document document, Set<CustomerAddress> address, OffsetDateTime registerAt) {
        this.setId(id);
        this.setFullName(fullName);
        this.setEmail(email);
        this.setCelPhone(celPhone);
        this.setDocument(document);
        this.setAddress(address);
        this.setRegisterAt(registerAt);
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

    public Document document() {
        return document;
    }

    public Set<CustomerAddress> customerAddresses() {
        return Collections.unmodifiableSet(address);
    }

    public OffsetDateTime registerAt() {
        return registerAt;
    }

    void addAddress(Address address, boolean isDeliveryAddress) {
        FieldValidator.requiresNonNull("address", address);
        FieldValidator.requiresNonNull("isDeliveryAddress", isDeliveryAddress);
        
        if (isDeliveryAddress) disableCurrentDeliveryAddress();

        var customerAddress = CustomerAddress.createNew()
                .customerId(this.id)
                .address(address)
                .isDeliveryAddress(isDeliveryAddress)
                .build();

        this.address.add(customerAddress);
    }

    void removeAddress(CustomerAddressId addressId) {
        var customerAddress = this.findCustomerAddress(addressId);

        this.verifyIfCanRemoveAddress(customerAddress);

        this.address.remove(customerAddress);
    }

    public void changeName(FullName fullName) {
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

    public void changeAddress(CustomerAddressId addressId, Address address, boolean isDeliveryAddress) {
        var customerAddress = findCustomerAddress(addressId);

        if (isDeliveryAddress) disableCurrentDeliveryAddress();

        customerAddress.changeAddress(address);
        customerAddress.changeDeliveryAddress(isDeliveryAddress);
    }

    private CustomerAddress findCustomerAddress(CustomerAddressId addressId) {
        return this.address.stream()
                .filter(customerAddress -> customerAddress.id().equals(addressId))
                .findFirst()
                .orElseThrow(() -> new CustomerAddressNotFoundException(""));
    }

    private void disableCurrentDeliveryAddress() {
        this.address.stream()
                .filter(CustomerAddress::isDeliveryAddress)
                .findFirst()
                .ifPresent(deliveryAddress -> deliveryAddress.changeDeliveryAddress(false));
    }

    private void verifyIfCanRemoveAddress(CustomerAddress customerAddress) {
        if (customerAddress.isDeliveryAddress() || this.address.size() == 1)
            throw new CannotDeleteDeliveryAddress("");
    }

    private void setId(CustomerId id) {
        FieldValidator.requiresNonNull("id", id);
        this.id = id;
    }

    private void setFullName(FullName fullName) {
        FieldValidator.requiresNonNull("fullName", fullName);
        this.fullName = fullName;
    }

    private void setEmail(Email email) {
        FieldValidator.requiresNonNull("email", email);
        this.email = email;
    }

    private void setCelPhone(CelPhone celPhone) {
        FieldValidator.requiresNonNull("celPhone", celPhone);
        this.celPhone = celPhone;
    }

    private void setDocument(Document document) {
        FieldValidator.requiresNonNull("document", document);
        this.document = document;
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
