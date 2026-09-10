package br.com.ipet.ordering.infrastructure.persistence.booking.appointment;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class ServiceEmbeddable {

    private UUID serviceId;
    private String type;
    private String category;
    private String description;
    private Integer duration;
    private BigDecimal price;
    private String petSize;

}
