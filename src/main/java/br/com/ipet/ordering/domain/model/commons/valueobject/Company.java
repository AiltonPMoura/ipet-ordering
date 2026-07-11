package br.com.ipet.ordering.domain.model.commons.valueobject;

import br.com.ipet.ordering.domain.model.FieldValidator;
import br.com.ipet.ordering.domain.model.commons.document.Document;
import br.com.ipet.ordering.domain.model.company.CompanyName;
import lombok.Builder;

@Builder
public record Company(CompanyName companyName, Document document,
                      Phone phone, Email email, Address address) {

    public Company {
        FieldValidator.requiresNonNull("companyName", companyName);
        FieldValidator.requiresNonNull("document", document);
        FieldValidator.requiresNonNull("celPhone", phone);
        FieldValidator.requiresNonNull("email", email);
        FieldValidator.requiresNonNull("address", address);
    }

}
