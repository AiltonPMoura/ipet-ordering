package br.com.ipet.ordering.domain.model.company;

import br.com.ipet.ordering.domain.model.commons.document.Cnpj;
import br.com.ipet.ordering.domain.model.commons.valueobject.Address;
import br.com.ipet.ordering.domain.model.commons.document.Document;
import br.com.ipet.ordering.domain.model.commons.valueobject.Phone;
import br.com.ipet.ordering.domain.model.commons.valueobject.Email;
import br.com.ipet.ordering.domain.model.commons.document.DocumentFactory;
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

        verifyEmailIsUnique(company.email(), company.id());
        verifyDocumentIsUnique(company.document(), company.id());

        return company;
    }

    public void changeEmail(Company company, Email newEmail) {
        verifyEmailIsUnique(newEmail, company.id());
        company.changeEmail(newEmail);
    }

    public void changeCnpj(Company company, Cnpj newCnpj) {
        verifyDocumentIsUnique(newCnpj, company.id());
        company.changeCnpj(newCnpj);
    }

    private void verifyDocumentIsUnique(Document document, CompanyId companyId) {
        if (!companies.isDocumentUnique(document, companyId)) {
            throw new CompanyDocumentIsInUseException();
        }
    }

    private void verifyEmailIsUnique(Email email, CompanyId companyId) {
        if (!companies.isEmailUnique(email, companyId))
            throw new CompanyEmailIsInUseException();
    }

}
