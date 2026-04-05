package br.com.ipet.ordering.infrastructure.persistence.order;

import br.com.ipet.ordering.infrastructure.persistence.commons.AddressEmbeddable;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class BillingEmbeddable {
    private String firstName;
    private String lastName;
    private String document;
    private String phone;
    private String email;

    @Embedded
    private AddressEmbeddable address;
}
