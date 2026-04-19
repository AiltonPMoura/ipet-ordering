package br.com.ipet.ordering.application.checkout;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CheckoutInput {
    private UUID shoppingCartId;
    private UUID companyId;
    private UUID customerId;
    private UUID customerAddressId;
    private String paymentMethod;
}
