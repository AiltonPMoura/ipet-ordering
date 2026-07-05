package br.com.ipet.ordering.presentation.pet;

import br.com.ipet.ordering.application.pet.management.PetInput;
import br.com.ipet.ordering.application.pet.management.PetManagementApplicationService;
import br.com.ipet.ordering.application.pet.management.PetUpdateInput;
import br.com.ipet.ordering.application.pet.query.PetDetailOutput;
import br.com.ipet.ordering.application.pet.query.PetFilter;
import br.com.ipet.ordering.application.pet.query.PetQueryService;
import br.com.ipet.ordering.application.pet.query.PetSummaryOutput;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

import java.util.UUID;

@RestController
@RequestMapping("/v1/customers/{customerId}/pets")
@RequiredArgsConstructor
public class PetController {

    private final PetManagementApplicationService petManagementApplicationService;
    private final PetQueryService petQueryService;

    @PostMapping
    public ResponseEntity<Void> create(@PathVariable UUID customerId, @RequestBody PetInput input) {
        var uuid = petManagementApplicationService.create(customerId, input);

        var url = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{petId}")
                .buildAndExpand(uuid)
                .toUri();

        return ResponseEntity.created(url).build();
    }

    @GetMapping
    public Page<PetSummaryOutput> filter(@PathVariable UUID customerId, PetFilter filter, Pageable pageable) {
        return petQueryService.filter(customerId, filter, pageable);
    }

    @GetMapping("/{petId}")
    public PetDetailOutput findById(@PathVariable UUID customerId, @PathVariable UUID petId) {
        return petQueryService.findById(customerId, petId);
    }

    @PutMapping("/{petId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable UUID customerId, @PathVariable UUID petId, @RequestBody PetUpdateInput input) {
        petManagementApplicationService.update(petId, customerId, input);
    }

    @DeleteMapping("/{petId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID customerId, @PathVariable UUID petId) {
        petManagementApplicationService.delete(petId, customerId);
    }

}
