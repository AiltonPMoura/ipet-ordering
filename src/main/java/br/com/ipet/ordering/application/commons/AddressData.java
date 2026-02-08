package br.com.ipet.ordering.application.commons;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddressData {
    private String street;
    private Integer number;
    private String neighborhood;
    private String complement;
    private String city;
    private String state;
    private Integer zipCode;
}
