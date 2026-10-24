package br.com.ipet.ordering.infrastructure.persistence.transport;

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
public class VehicleEmbeddable {

    private String licensePlate;
    private String brand;
    private String model;

}
