package br.com.ipet.ordering.application.company.management;

import br.com.ipet.ordering.application.commons.AddressData;
import br.com.ipet.ordering.domain.model.commons.valueobject.Address;
import br.com.ipet.ordering.domain.model.commons.valueobject.Phone;
import br.com.ipet.ordering.domain.model.company.CompanyNotFoundException;
import br.com.ipet.ordering.domain.model.company.CompanyId;
import br.com.ipet.ordering.domain.model.company.CompanyName;
import br.com.ipet.ordering.domain.model.commons.valueobject.Email;
import br.com.ipet.ordering.domain.model.commons.valueobject.ZipCode;
import br.com.ipet.ordering.domain.model.company.Companies;
import br.com.ipet.ordering.domain.model.company.Company;
import br.com.ipet.ordering.domain.model.company.CompanyService;
import br.com.ipet.ordering.domain.model.FieldValidator;
import br.com.ipet.ordering.domain.model.commons.document.DocumentFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CompanyApplicationService {

    private final CompanyService companyService;
    private final Companies companies;
    private final DocumentFactory documentFactory;

    public UUID create(CompanyInput input) {
        FieldValidator.requiresNonNull("company input", input);

        var address = input.getAddress();

        var company = companyService.register(
                new CompanyName(input.getCompanyName()),
                documentFactory.from(input.getDocument()),
                new Phone(input.getPhone()),
                new Email(input.getEmail()),
                this.toAddress(address)
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
        company.changeDocument(documentFactory.from(input.getDocument()));
        company.changeAddress(this.toAddress(input.getAddress()));

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
                .orElseThrow(CompanyNotFoundException::new);
    }

    private Address toAddress(AddressData address) {
        return Address.builder()
                .street(address.getStreet())
                .neighborhood(address.getNeighborhood())
                .number(address.getNumber())
                .city(address.getCity())
                .state(address.getState())
                .complement(address.getComplement())
                .zipCode(new ZipCode(address.getZipCode()))
                .build();
    }

}
