package br.com.ipet.ordering.domain.model.company;

import br.com.ipet.ordering.domain.model.commons.document.Document;
import br.com.ipet.ordering.domain.model.commons.valueobject.Address;
import br.com.ipet.ordering.domain.model.commons.valueobject.Email;
import br.com.ipet.ordering.domain.model.commons.valueobject.Phone;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CompanyService {

    private final Companies companies;

    public Company register(CompanyName name, Document document,
                            Phone phone, Email email, Address address) {

        var company = Company.createNew()
                .name(name)
                .document(document)
                .phone(phone)
                .email(email)
                .address(address)
                .build();

        this.verifyEmailIsUnique(company.email(), company.id());

        return company;
    }

    public void changeEmail(Company company, Email newEmail) {
        this.verifyEmailIsUnique(newEmail, company.id());
        company.changeEmail(newEmail);
    }

    private void verifyEmailIsUnique(Email email, CompanyId companyId) {
        if (!companies.isEmailUnique(email, companyId))
            throw new CompanyEmailIsInUseException();
    }

}
