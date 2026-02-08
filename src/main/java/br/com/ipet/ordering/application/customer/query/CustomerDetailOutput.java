package br.com.ipet.ordering.application.customer.query;


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
public class CustomerDetailOutput {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private Integer celPhone;
    private String document;
    private AddressData address;
    private OffsetDateTime registerAt;
}
