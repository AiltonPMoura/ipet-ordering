package br.com.ipet.ordering.infrastructure.persistence.embedded;

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
public class ProductEmbeddable {
    private String name;
    private String description;
    private BigDecimal price;
}
