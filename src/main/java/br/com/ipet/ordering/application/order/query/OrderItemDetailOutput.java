package br.com.ipet.ordering.application.order.query;

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
public class OrderItemDetailOutput {
    private UUID id;
    private UUID orderId;
    private ProductData product;
    private Integer quantity;
    private BigDecimal totalAmount;
}
