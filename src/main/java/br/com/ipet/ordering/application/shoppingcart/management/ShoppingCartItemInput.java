package br.com.ipet.ordering.application.shoppingcart.management;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingCartItemInput {
    private UUID shoppingCartId;
    private UUID customerId;
    private UUID companyId;
    private UUID productId;
    private Integer quantity;
}
