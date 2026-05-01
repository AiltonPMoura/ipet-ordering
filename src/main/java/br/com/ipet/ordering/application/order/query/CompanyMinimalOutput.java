package br.com.ipet.ordering.application.order.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompanyMinimalOutput {
    private String companyName;
    private String document;
    private String phone;
    private String email;
}
