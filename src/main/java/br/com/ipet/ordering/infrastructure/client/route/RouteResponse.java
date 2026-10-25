package br.com.ipet.ordering.infrastructure.client.route;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RouteResponse {
    private String routeCost;
    private Long estimatedDays;
}
