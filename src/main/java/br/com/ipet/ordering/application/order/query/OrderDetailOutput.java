package br.com.ipet.ordering.application.order.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailOutput {
    private UUID id;
    private CustomerMinimalOutput customer;
    private CompanyMinimalOutput company;
    private BigDecimal totalAmount;
    private Integer totalItems;
    private String paymentMethod;
    private String status;
    private ShippingData shipping;
    private BillingData billing;
    private DeliveryCompanyData deliveryCompany;
    private OffsetDateTime placedAt;
    private OffsetDateTime readyAt;
    private OffsetDateTime paidAt;
    private OffsetDateTime outForDeliveryAt;
    private OffsetDateTime deliveredAt;
    private OffsetDateTime canceledAt;
    private List<OrderItemDetailOutput> items;
}
