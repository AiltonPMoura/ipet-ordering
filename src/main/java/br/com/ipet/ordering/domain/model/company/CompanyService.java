package br.com.ipet.ordering.domain.model.company;

import br.com.ipet.ordering.domain.model.commons.valueobject.Address;
import br.com.ipet.ordering.domain.model.commons.valueobject.CelPhone;
import br.com.ipet.ordering.domain.model.commons.valueobject.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
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
        verifyCnpjIsUnique(company.cnpj(), company.id());

        return company;
    }

    public void changeEmail(Company company, Email newEmail) {
        verifyEmailIsUnique(newEmail, company.id());
        company.changeEmail(newEmail);
    }

    public void changeCnpj(Company company, Cnpj newCnpj) {
        verifyCnpjIsUnique(newCnpj, company.id());
        company.changeCnpj(newCnpj);
    }

    private void verifyCnpjIsUnique(Cnpj cnpj, CompanyId companyId) {
        if (!companies.isCnpjUnique(cnpj, companyId)) {
            throw new CompanyCnpjIsInUseException();
        }
    }

    private void verifyEmailIsUnique(Email email, CompanyId companyId) {
        if (!companies.isEmailUnique(email, companyId))
            throw new CompanyEmailIsInUseException();
    }

}
