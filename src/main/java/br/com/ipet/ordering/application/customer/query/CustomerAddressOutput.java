package br.com.ipet.ordering.application.customer.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerAddressOutput {
    private UUID id;
    private String street;
    private Integer number;
    private String neighborhood;
    private String complement;
    private String city;
    private String state;
    private String zipCode;
    private boolean isPrincipal;
}
