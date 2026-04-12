package br.com.ipet.ordering.infrastructure.client.shipping;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryCostRequest {
    private String originZipCode;
    private String destinationZipCode;
}
