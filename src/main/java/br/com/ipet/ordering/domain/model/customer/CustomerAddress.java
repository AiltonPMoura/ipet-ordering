package br.com.ipet.ordering.domain.model.customer;

import br.com.ipet.ordering.domain.model.FieldValidator;
import br.com.ipet.ordering.domain.model.commons.valueobject.Address;
import lombok.Builder;

import java.util.Objects;

public class CustomerAddress {
    private CustomerAddressId id;
    private CustomerId customerId;
    private Address address;
    private boolean isPrincipal;

    static CustomerAddress create(CustomerId customerId, Address address, boolean isPrincipal) {
        return new CustomerAddress(new CustomerAddressId(), customerId, address, isPrincipal);
    }

    @Builder(builderClassName = "ExistingCustomerAddressBuilder", builderMethodName = "existing")
    private CustomerAddress(CustomerAddressId id, CustomerId customerId,
                           Address address, boolean isPrincipal) {
        this.setId(id);
        this.setCustomerId(customerId);
        this.setAddress(address);
        this.setIsPrincipal(isPrincipal);
    }

    void changeAddress(Address address) {
        this.setAddress(address);
    }

    void changePrincipal(boolean isPrincipal) {
        this.setIsPrincipal(isPrincipal);
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

    public boolean isPrincipal() {
        return isPrincipal;
    }

    private void setIsPrincipal(boolean isPrincipal) {
        FieldValidator.requiresNonNull("isPrincipal", isPrincipal);
        this.isPrincipal = isPrincipal;
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
