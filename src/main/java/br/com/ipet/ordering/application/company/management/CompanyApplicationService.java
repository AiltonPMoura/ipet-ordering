package br.com.ipet.ordering.application.company.management;

import br.com.ipet.ordering.domain.model.commons.valueobject.Address;
import br.com.ipet.ordering.domain.model.commons.valueobject.CelPhone;
import br.com.ipet.ordering.domain.model.company.CompanyNotFoundException;
import br.com.ipet.ordering.domain.model.customer.Cnpj;
import br.com.ipet.ordering.domain.model.customer.CompanyId;
import br.com.ipet.ordering.domain.model.customer.CompanyName;
import br.com.ipet.ordering.domain.model.commons.valueobject.Email;
import br.com.ipet.ordering.domain.model.commons.valueobject.ZipCode;
import br.com.ipet.ordering.domain.model.company.Companies;
import br.com.ipet.ordering.domain.model.company.Company;
import br.com.ipet.ordering.domain.model.company.CompanyService;
import br.com.ipet.ordering.domain.model.FieldValidator;
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

    public UUID create(CompanyInput input) {
        FieldValidator.requiresNonNull("company input", input);
        var address = input.getAddress();

        var company = companyService.register(
                new CompanyName(input.getCompanyName()),
                new Cnpj(input.getCnpj()),
                new CelPhone(input.getCelPhone()),
                new Email(input.getEmail()),
                Address.builder()
                        .street(address.getStreet())
                        .neighborhood(address.getNeighborhood())
                        .number(address.getNumber())
                        .city(address.getCity())
                        .state(address.getState())
                        .complement(address.getComplement())
                        .zipCode(new ZipCode(address.getZipCode()))
                        .build()
        );

        companies.add(company);

        return company.id().value();
    }

    public void update(CompanyUpdateInput input, UUID companyId) {
        FieldValidator.requiresNonNull("company input", input);
        FieldValidator.requiresNonNull("company id", companyId);

        var company = findCompanyById(companyId);
        var address = input.getAddress();

        company.changeCompanyName(new CompanyName(input.getCompanyName()));
        company.changeCelPhone(new CelPhone(input.getCelPhone()));
        company.changeAddress(Address.builder()
                        .street(address.getStreet())
                        .number(address.getNumber())
                        .neighborhood(address.getNeighborhood())
                        .complement(address.getComplement())
                        .city(address.getCity())
                        .state(address.getState())
                        .zipCode(new ZipCode(address.getZipCode()))
                .build());

        companies.add(company);
    }

    public void changeEmail(String newEmail, UUID companyId) {
        FieldValidator.requiresNonNull("email", newEmail);
        FieldValidator.requiresNonNull("companyId", companyId);

        var company = findCompanyById(companyId);
        companyService.changeEmail(company, new Email(newEmail));
        companies.add(company);
    }

    public void changeCnpj(String newCnpj, UUID companyId) {
        FieldValidator.requiresNonNull("cnpj", newCnpj);
        FieldValidator.requiresNonNull("companyId", companyId);

        var company = findCompanyById(companyId);
        companyService.changeCnpj(company, new Cnpj(newCnpj));
        companies.add(company);
    }

    private Company findCompanyById(UUID companyId) {
        return companies.ofId(new CompanyId(companyId))
                .orElseThrow(CompanyNotFoundException::new);
    }

}
