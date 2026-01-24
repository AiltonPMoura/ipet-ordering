package br.com.ipet.ordering.domain.model.company;

import br.com.ipet.ordering.domain.model.commons.Address;
import br.com.ipet.ordering.domain.model.commons.CelPhone;
import br.com.ipet.ordering.domain.model.commons.Cnpj;
import br.com.ipet.ordering.domain.model.commons.CompanyId;
import br.com.ipet.ordering.domain.model.commons.CompanyName;
import br.com.ipet.ordering.domain.model.commons.Email;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CompanyService {

    private final Companies companies;

    public Company register(CompanyName name, Cnpj cnpj,
                            CelPhone celPhone, Email email, Address address) {

        var company = Company.createNew()
                .name(name)
                .cnpj(cnpj)
                .celPhone(celPhone)
                .email(email)
                .address(address)
                .build();

        verifyEmailIsUnique(company.email(), company.id());

        return company;

    }

    public void changeEmail(Company company, Email newEmail) {
        verifyEmailIsUnique(newEmail, company.id());
        company.changeEmail(newEmail);
    }

    private void verifyEmailIsUnique(Email email, CompanyId companyId) {
        if (!companies.isEmailUnique(email, companyId))
            throw new CompanyEmailIsInUseException();
    }

}
