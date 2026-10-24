package br.com.ipet.ordering.presentation.transport;

import br.com.ipet.ordering.application.transport.management.PetTransportInput;
import br.com.ipet.ordering.application.transport.management.PetTransportManagementApplicationService;
import br.com.ipet.ordering.application.transport.management.PetTransportUpdateInput;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.UUID;

@RestController
@RequestMapping("/v1/pet-transports")
@RequiredArgsConstructor
public class PetTransportController {

    private final PetTransportManagementApplicationService applicationService;

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody PetTransportInput input) {
        var petTransportId = applicationService.create(input);

        var location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{petTransportId}")
                .buildAndExpand(petTransportId)
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{petTransportId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable UUID petTransportId,
                       @RequestParam UUID companyId,// Obter da autenticação do usuário logado
                       @RequestBody PetTransportUpdateInput input) {
        applicationService.update(companyId, petTransportId, input);
    }

    @DeleteMapping("/{petTransportId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID petTransportId,
                       @RequestParam UUID companyId /* Obter da autenticação do usuário logado */) {
        applicationService.delete(companyId, petTransportId);
    }
}
