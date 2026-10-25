package br.com.ipet.ordering.infrastructure.client.route;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RouteCostResponse {
    private String deliveryCost;
}
