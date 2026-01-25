package br.com.ipet.ordering.application.model.company;

import br.com.ipet.ordering.application.model.commons.AdressData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompanyInput {
    private String name;
    private String email;
    private Integer celPhone;
    private String cnpj;
    private AdressData address;
}
