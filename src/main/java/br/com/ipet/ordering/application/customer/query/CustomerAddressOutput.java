package br.com.ipet.ordering.application.customer.query;

import br.com.ipet.ordering.domain.model.commons.valueobject.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerAddressOutput {
    private Address address;
    private boolean isPrincipal;
}
