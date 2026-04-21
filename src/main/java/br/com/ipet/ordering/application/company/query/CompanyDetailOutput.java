package br.com.ipet.ordering.application.company.query;

import br.com.ipet.ordering.application.commons.AddressData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompanyDetailOutput {
    private UUID id;
    private String companyName;
    private String document;
    private String phone;
    private String email;
    private AddressData address;
    private OffsetDateTime registeredAt;
}
