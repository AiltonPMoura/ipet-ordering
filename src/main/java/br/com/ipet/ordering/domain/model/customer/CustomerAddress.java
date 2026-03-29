package br.com.ipet.ordering.domain.model.customer;

import br.com.ipet.ordering.domain.model.FieldValidator;
import br.com.ipet.ordering.domain.model.commons.valueobject.Address;
import lombok.Builder;

public class CustomerAddress {
    private CustomerAddressId id;
    private CustomerId customerId;
    private Address address;
    private boolean isDeliveryAddress;

    @Builder(builderClassName = "CreateNewCustomerAddressBuilder", builderMethodName = "createNew")
    private static CustomerAddress create(CustomerId customerId, Address address, boolean isDeliveryAddress) {
        return new CustomerAddress(new CustomerAddressId(), customerId, address, isDeliveryAddress);
    }

    @Builder(builderClassName = "ExistingCustomerAddressBuilder", builderMethodName = "existing")
    private CustomerAddress(CustomerAddressId id, CustomerId customerId,
                           Address address, boolean isDeliveryAddress) {
        this.setId(id);
        this.setCustomerId(customerId);
        this.setAddress(address);
        this.setIsDeliveryAddress(isDeliveryAddress);
    }

    public CustomerAddressId id() {
        return id;
    }

    public CustomerId customerId() {
        return customerId;
    }

    public Address address() {
        return address;
    }

    public boolean isDeliveryAddress() {
        return isDeliveryAddress;
    }

    void changeAddress(Address address) {
        this.setAddress(address);
    }

    void changeDeliveryAddress(boolean isDeliveryAddress) {
        this.setIsDeliveryAddress(isDeliveryAddress);
    }

    private void setId(CustomerAddressId id) {
        FieldValidator.requiresNonNull("id", id);
        this.id = id;
    }

    private void setCustomerId(CustomerId customerId) {
        FieldValidator.requiresNonNull("customerId", customerId);
        this.customerId = customerId;
    }

    private void setAddress(Address address) {
        FieldValidator.requiresNonNull("address", address);
        this.address = address;
    }

    private void setIsDeliveryAddress(boolean deliveryAddress) {
        FieldValidator.requiresNonNull("deliveryAddress", deliveryAddress);
        this.isDeliveryAddress = deliveryAddress;
    }
}
