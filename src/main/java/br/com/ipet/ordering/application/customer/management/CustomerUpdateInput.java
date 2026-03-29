package br.com.ipet.ordering.application.customer.management;

import br.com.ipet.ordering.application.commons.AddressData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerUpdateInput {
    private String firstName;
    private String lastName;
    private Integer celPhone;
    private String document;
    private AddressData addressData;
    private boolean isDeliveryAddress;
}
