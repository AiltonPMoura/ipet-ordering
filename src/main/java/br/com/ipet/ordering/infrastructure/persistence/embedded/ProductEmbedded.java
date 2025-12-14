package br.com.ipet.ordering.infrastructure.persistence.embedded;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductEmbedded {
    private String name;
    private String description;
    private BigDecimal price;
}
