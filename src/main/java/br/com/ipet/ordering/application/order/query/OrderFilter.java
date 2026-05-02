package br.com.ipet.ordering.application.order.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderFilter {
    private UUID customerId;
    private OffsetDateTime startTime;
    private OffsetDateTime endTime;
    private String status;
}
