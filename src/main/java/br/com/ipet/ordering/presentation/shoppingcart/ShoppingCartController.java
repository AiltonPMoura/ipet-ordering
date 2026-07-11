package br.com.ipet.ordering.presentation.shoppingcart;

import br.com.ipet.ordering.application.shoppingcart.management.ShoppingCartManagementApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.UUID;

@RestController
@RequestMapping("/v1/customers/{customerId}/shopping-carts")
@RequiredArgsConstructor
public class ShoppingCartController {

    private final ShoppingCartManagementApplicationService shoppingCartManagementApplicationService;

    @PostMapping
    public ResponseEntity<Void> create(@PathVariable UUID customerId) {
        var uuid = shoppingCartManagementApplicationService.create(customerId);

        var url = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{shoppingCartId}")
                .buildAndExpand(uuid)
                .toUri();

        return ResponseEntity.created(url).build();
    }

    @PutMapping("/{shoppingCartId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void empty(@PathVariable UUID customerId,
                      @PathVariable UUID shoppingCartId) {
        shoppingCartManagementApplicationService.empty(shoppingCartId, customerId);
    }

    @DeleteMapping("/{shoppingCartId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID customerId,
                       @PathVariable UUID shoppingCartId) {
        shoppingCartManagementApplicationService.delete(shoppingCartId, customerId);
    }

}
