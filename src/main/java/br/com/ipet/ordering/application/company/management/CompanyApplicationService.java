package br.com.ipet.ordering.application.company.management;

import br.com.ipet.ordering.domain.model.commons.Address;
import br.com.ipet.ordering.domain.model.commons.CelPhone;
import br.com.ipet.ordering.domain.model.company.CompanyNotFoundException;
import br.com.ipet.ordering.domain.model.customer.Cnpj;
import br.com.ipet.ordering.domain.model.customer.CompanyId;
import br.com.ipet.ordering.domain.model.customer.CompanyName;
import br.com.ipet.ordering.domain.model.commons.Email;
import br.com.ipet.ordering.domain.model.commons.ZipCode;
import br.com.ipet.ordering.domain.model.company.Companies;
import br.com.ipet.ordering.domain.model.company.Company;
import br.com.ipet.ordering.domain.model.company.CompanyService;
import br.com.ipet.ordering.domain.model.FieldValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CompanyApplicationService {

    private final CompanyService companyService;
    private final Companies companies;

    public UUID create(CompanyInput input) {
        FieldValidator.requiresNonNull("company input", input);
        var address = input.getAddress();

        var company = companyService.register(
                new CompanyName(input.getName()),
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

    public UUID update(CompanyUpdate update, UUID companyId) {
        var company = findCompanyById(companyId);
        var address = update.getAddress();

        company.changeCompanyName(new CompanyName(update.getName()));
        company.changeCelPhone(new CelPhone(update.getCelPhone()));
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

        return company.id().value();
    }

    public UUID changeEmail(String newEmail, UUID companyId) {
        var company = findCompanyById(companyId);
        companyService.changeEmail(company, new Email(newEmail));
        return company.id().value();
    }

    public UUID changeCnpj(String newCnpj, UUID companyId) {
        var company = findCompanyById(companyId);
        companyService.changeCnpj(company, new Cnpj(newCnpj));
        return company.id().value();
    }

    private Company findCompanyById(UUID companyId) {
        return companies.ofId(new CompanyId(companyId))
                .orElseThrow(CompanyNotFoundException::new);
    }

}
