package br.com.ipet.ordering.domain.model.order;

import br.com.ipet.ordering.domain.model.FieldValidator;
import br.com.ipet.ordering.domain.model.commons.document.Document;
import br.com.ipet.ordering.domain.model.commons.valueobject.Address;
import br.com.ipet.ordering.domain.model.commons.valueobject.Phone;
import br.com.ipet.ordering.domain.model.commons.valueobject.Email;
import br.com.ipet.ordering.domain.model.commons.valueobject.FullName;
import lombok.Builder;

@Builder
public record Billing(FullName fullName, Document document,
                      Phone phone, Email email, Address address) {

    public Billing {
        FieldValidator.requiresNonNull("fullName", fullName);
        FieldValidator.requiresNonNull("document", document);
        FieldValidator.requiresNonNull("celPhone", phone);
        FieldValidator.requiresNonNull("email", email);
        FieldValidator.requiresNonNull("address", address);
    }

}
