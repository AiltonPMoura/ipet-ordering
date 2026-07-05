package br.com.ipet.ordering.presentation.shoppingcart;

import br.com.ipet.ordering.application.shoppingcart.management.ShoppingCartItemInput;
import br.com.ipet.ordering.application.shoppingcart.management.ShoppingCartManagementApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/v1/customers/{customerId}/shopping-carts/{shoppingCartId}/items")
@RequiredArgsConstructor
public class ShoppingCartItemController {

    private final ShoppingCartManagementApplicationService shoppingCartManagementApplicationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addItem(@PathVariable UUID customerId,
                        @PathVariable UUID shoppingCartId,
                        @RequestBody ShoppingCartItemInput input) {
        shoppingCartManagementApplicationService.addItem(customerId, shoppingCartId, input);
    }

    @DeleteMapping("/{itemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeItem(@PathVariable UUID customerId,
                           @PathVariable UUID shoppingCartId,
                           @PathVariable UUID itemId) {
        shoppingCartManagementApplicationService.removeItem(shoppingCartId, itemId, customerId);
    }

}
