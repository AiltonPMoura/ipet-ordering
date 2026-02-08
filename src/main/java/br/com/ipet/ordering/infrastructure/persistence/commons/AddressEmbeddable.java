package br.com.ipet.ordering.infrastructure.persistence.commons;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class AddressEmbeddable {
    private String street;
    private Integer number;
    private String neighborhood;
    private String complement;
    private String city;
    private String state;
    private Integer zipCode;
}
