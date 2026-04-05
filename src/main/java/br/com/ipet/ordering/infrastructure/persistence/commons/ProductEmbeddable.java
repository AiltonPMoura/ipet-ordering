package br.com.ipet.ordering.infrastructure.persistence.commons;

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
public class ProductEmbeddable {
    private UUID productId;
    private String name;
    private String description;
    private BigDecimal price;
}
