package br.com.ipet.ordering.domain.model.customer;

import br.com.ipet.ordering.domain.model.FieldValidator;
import br.com.ipet.ordering.domain.model.commons.valueobject.Address;
import lombok.Builder;

import java.util.Objects;

public class CustomerAddress {
    private CustomerAddressId id;
    private CustomerId customerId;
    private Address address;
    private boolean isPrincipalAddress;

    @Builder(builderClassName = "CreateNewCustomerAddressBuilder", builderMethodName = "createNew")
    private static CustomerAddress create(CustomerId customerId, Address address, boolean isPrincipalAddress) {
        return new CustomerAddress(new CustomerAddressId(), customerId, address, isPrincipalAddress);
    }

    @Builder(builderClassName = "ExistingCustomerAddressBuilder", builderMethodName = "existing")
    private CustomerAddress(CustomerAddressId id, CustomerId customerId,
                           Address address, boolean isPrincipalAddress) {
        this.setId(id);
        this.setCustomerId(customerId);
        this.setAddress(address);
        this.setIsPrincipalAddress(isPrincipalAddress);
    }

    void changeAddress(Address address) {
        this.setAddress(address);
    }

    void changePrincipalAddress(boolean isPrincipalAddress) {
        this.setIsPrincipalAddress(isPrincipalAddress);
    }

    public CustomerAddressId id() {
        return id;
    }

    private void setId(CustomerAddressId id) {
        FieldValidator.requiresNonNull("id", id);
        this.id = id;
    }

    public CustomerId customerId() {
        return customerId;
    }

    private void setCustomerId(CustomerId customerId) {
        FieldValidator.requiresNonNull("customerId", customerId);
        this.customerId = customerId;
    }

    public Address address() {
        return address;
    }

    private void setAddress(Address address) {
        FieldValidator.requiresNonNull("address", address);
        this.address = address;
    }

    public boolean isPrincipalAddress() {
        return isPrincipalAddress;
    }

    private void setIsPrincipalAddress(boolean isPrincipalAddress) {
        FieldValidator.requiresNonNull("isPrincipalAddress", isPrincipalAddress);
        this.isPrincipalAddress = isPrincipalAddress;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof CustomerAddress that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
