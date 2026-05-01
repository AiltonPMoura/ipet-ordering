package br.com.ipet.ordering.application.order.query;

import br.com.ipet.ordering.application.commons.AddressData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryCompanyData {
    private String companyName;
    private String document;
    private String phone;
    private String email;
    private AddressData address;
}
