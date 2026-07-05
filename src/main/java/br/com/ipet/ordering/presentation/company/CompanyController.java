package br.com.ipet.ordering.presentation.company;

import br.com.ipet.ordering.application.company.management.CompanyInput;
import br.com.ipet.ordering.application.company.management.CompanyManagementApplicationService;
import br.com.ipet.ordering.application.company.management.CompanyUpdateInput;
import br.com.ipet.ordering.application.commons.EmailInput;
import br.com.ipet.ordering.application.company.query.CompanyDetailOutput;
import br.com.ipet.ordering.application.company.query.CompanyFilter;
import br.com.ipet.ordering.application.company.query.CompanyQueryService;
import br.com.ipet.ordering.application.company.query.CompanySummaryOutput;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
@RequestMapping("/v1/companies")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyManagementApplicationService companyManagementApplicationService;
    private final CompanyQueryService companyQueryService;

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody CompanyInput input) {
        var uuid = companyManagementApplicationService.create(input);

        var url = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{companyId}")
                .buildAndExpand(uuid)
                .toUri();

        return ResponseEntity.created(url).build();
    }

    @GetMapping
    public Page<CompanySummaryOutput> filter(CompanyFilter filter, Pageable pageable) {
        return companyQueryService.filter(filter, pageable);
    }

    @GetMapping("/{companyId}")
    public CompanyDetailOutput findById(@PathVariable UUID companyId) {
        return companyQueryService.findById(companyId);
    }

    @PutMapping("/{companyId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable UUID companyId, @RequestBody CompanyUpdateInput input) {
        companyManagementApplicationService.update(companyId, input);
    }

    @DeleteMapping("/{companyId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID companyId) {
        //companyManagementApplicationService.delete(companyId);
    }

    @PatchMapping("/{companyId}/email")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changeEmail(@PathVariable UUID companyId, @RequestBody EmailInput email) {
        companyManagementApplicationService.changeEmail(companyId, email.getEmail());
    }

}
