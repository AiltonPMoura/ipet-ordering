package br.com.ipet.ordering.application.company;

import br.com.ipet.ordering.application.commons.AdressData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompanyUpdate {
    private String name;
    private Integer celPhone;
    private AdressData address;
}
