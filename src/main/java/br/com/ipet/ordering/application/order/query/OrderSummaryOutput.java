package br.com.ipet.ordering.application.order.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderSummaryOutput {
    private UUID id;
    private BigDecimal totalAmount;
    private Integer totalItems;
    private String status;
    private OffsetDateTime placedAt;
}
