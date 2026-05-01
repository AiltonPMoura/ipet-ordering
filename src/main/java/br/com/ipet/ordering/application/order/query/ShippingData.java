package br.com.ipet.ordering.application.order.query;

import br.com.ipet.ordering.application.commons.AddressData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShippingData {
    private BigDecimal cost;
    private LocalDate expectedDate;
    private AddressData addressData;
}
