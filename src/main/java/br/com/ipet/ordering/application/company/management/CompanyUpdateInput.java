package br.com.ipet.ordering.application.company.management;

import br.com.ipet.ordering.application.commons.AddressData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompanyUpdateInput {
    private String companyName;
    private String document;
    private String phone;
    private AddressData address;
}
