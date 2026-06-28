package br.com.ipet.ordering.presentation.customer;

import br.com.ipet.ordering.application.commons.AddressData;
import br.com.ipet.ordering.application.customer.management.CustomerManagementApplicationService;
import br.com.ipet.ordering.application.customer.query.CustomerAddressOutput;
import br.com.ipet.ordering.application.customer.query.CustomerQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/customers/{customerId}/addresses")
@RequiredArgsConstructor
public class CustomerAddressController {

    private final CustomerManagementApplicationService customerManagementApplicationService;
    private final CustomerQueryService customerQueryService;

    @PostMapping
    public ResponseEntity<Void> create(@PathVariable UUID customerId, @RequestBody AddressData address) {
        var uuid = customerManagementApplicationService.createAddress(customerId, address);

        var url = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{addressId}")
                .buildAndExpand(uuid)
                .toUri();

        return ResponseEntity.created(url).build();
    }

    @GetMapping
    public List<CustomerAddressOutput> findAll(@PathVariable UUID customerId) {
        return customerQueryService.findAddressesByCustomerId(customerId);
    }

    @PutMapping("/{addressId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateAddress(@PathVariable UUID customerId, @PathVariable UUID addressId, @RequestBody AddressData address) {
        customerManagementApplicationService.changeAddress(customerId, addressId, address);
    }

    @PutMapping("/{addressId}/principal")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changePrincipalAddress(@PathVariable UUID customerId, @PathVariable UUID addressId) {
        customerManagementApplicationService.changePrincipalAddress(customerId, addressId);
    }

    @DeleteMapping("/{addressId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeAddress(@PathVariable UUID customerId, @PathVariable UUID addressId) {
        customerManagementApplicationService.removeAddress(customerId, addressId);
    }

}
