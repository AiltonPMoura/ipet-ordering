package br.com.ipet.ordering.presentation.customer;

import br.com.ipet.ordering.application.commons.AddressData;
import br.com.ipet.ordering.application.customer.management.CustomerInput;
import br.com.ipet.ordering.application.customer.management.CustomerManagementApplicationService;
import br.com.ipet.ordering.application.customer.management.CustomerUpdateInput;
import br.com.ipet.ordering.application.customer.query.CustomerDetailOutput;
import br.com.ipet.ordering.application.customer.query.CustomerFilter;
import br.com.ipet.ordering.application.customer.query.CustomerQueryService;
import br.com.ipet.ordering.application.customer.query.CustomerSummaryOutput;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.UUID;

@RestController
@RequestMapping("/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerManagementApplicationService customerManagementApplicationService;
    private final CustomerQueryService customerQueryService;

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody CustomerInput input) {
        var uuid = customerManagementApplicationService.create(input);

        var url = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{customerId}")
                .buildAndExpand(uuid)
                .toUri();

        return ResponseEntity.created(url).build();
    }

    @GetMapping
    public Page<CustomerSummaryOutput> filter(CustomerFilter filter, Pageable pageable) {
        return customerQueryService.filter(filter, pageable);
    }

    @GetMapping("/{customerId}")
    public CustomerDetailOutput findById(@PathVariable UUID customerId) {
        return customerQueryService.findById(customerId);
    }

    @PutMapping("/{customerId}")
    public void update(@PathVariable UUID customerId, @RequestBody CustomerUpdateInput input) {
        customerManagementApplicationService.update(customerId, input);
    }

    @PatchMapping("/{customerId}/email")
    public void changeEmail(@PathVariable UUID customerId, @RequestBody String email) {
        customerManagementApplicationService.changeEmail(customerId, email);
    }

    @PostMapping("/{customerId}/address")
    public ResponseEntity<Void> createAddress(@PathVariable UUID customerId, @RequestBody AddressData address) {
        var uuid = customerManagementApplicationService.createAddress(customerId, address);

        var url = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{addressId}")
                .buildAndExpand(uuid)
                .toUri();

        return ResponseEntity.created(url).build();
    }

    @PutMapping("/{customerId}/address/{addressId}")
    public void updateAddress(@PathVariable UUID customerId, @PathVariable UUID addressId, @RequestBody AddressData address) {
        customerManagementApplicationService.changeAddress(customerId, addressId, address);
    }

    @PatchMapping("/{customerId}/address/{addressId}")
    public void changePrincipalAddress(@PathVariable UUID customerId, @PathVariable UUID addressId) {
        customerManagementApplicationService.changePrincipalAddress(customerId, addressId);
    }

    @DeleteMapping("/{customerId}/addresses/{addressId}")
    public void removeAddress(@PathVariable UUID customerId, @PathVariable UUID addressId) {
        customerManagementApplicationService.removeAddress(customerId, addressId);
    }

}
