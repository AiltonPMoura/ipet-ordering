package br.com.ipet.ordering.application.company.management;

import br.com.ipet.ordering.application.commons.AddressMapper;
import br.com.ipet.ordering.domain.model.FieldValidator;
import br.com.ipet.ordering.domain.model.commons.document.DocumentFactory;
import br.com.ipet.ordering.domain.model.commons.valueobject.Email;
import br.com.ipet.ordering.domain.model.commons.valueobject.Phone;
import br.com.ipet.ordering.domain.model.company.Companies;
import br.com.ipet.ordering.domain.model.company.Company;
import br.com.ipet.ordering.domain.model.company.CompanyId;
import br.com.ipet.ordering.domain.model.company.CompanyName;
import br.com.ipet.ordering.domain.model.company.CompanyNotFoundException;
import br.com.ipet.ordering.domain.model.company.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CompanyManagementApplicationService {

    private final CompanyService companyService;
    private final Companies companies;
    private final AddressMapper addressMapper;

    public UUID create(CompanyInput input) {
        FieldValidator.requiresNonNull("input", input);

        var company = companyService.register(
                new CompanyName(input.getCompanyName()),
                DocumentFactory.from(input.getDocument()),
                new Phone(input.getPhone()),
                new Email(input.getEmail()),
                addressMapper.toAddress(input.getAddress())
        );

        companies.add(company);

        return company.id().value();
    }

    public void update(UUID companyId, CompanyUpdateInput input) {
        FieldValidator.requiresNonNull("company id", companyId);
        FieldValidator.requiresNonNull("company input", input);

        var company = this.findCompanyById(companyId);

        company.changeCompanyName(new CompanyName(input.getCompanyName()));
        company.changePhone(new Phone(input.getPhone()));
        company.changeDocument(DocumentFactory.from(input.getDocument()));
        company.changeAddress(addressMapper.toAddress(input.getAddress()));

        companies.add(company);
    }

    public void changeEmail(UUID companyId, String newEmail) {
        FieldValidator.requiresNonNull("companyId", companyId);
        FieldValidator.requiresNonNull("email", newEmail);

        var company = this.findCompanyById(companyId);

        companyService.changeEmail(company, new Email(newEmail));
        companies.add(company);
    }

    private Company findCompanyById(UUID companyId) {
        return companies.ofId(new CompanyId(companyId))
                .orElseThrow(() -> new CompanyNotFoundException(""));
    }

}
