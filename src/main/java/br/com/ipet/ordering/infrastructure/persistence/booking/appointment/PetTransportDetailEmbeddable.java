package br.com.ipet.ordering.infrastructure.persistence.booking.appointment;

import br.com.ipet.ordering.infrastructure.persistence.commons.AddressEmbeddable;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class PetTransportDetailEmbeddable {

    private BigDecimal cost;
    private AddressEmbeddable address;

}
