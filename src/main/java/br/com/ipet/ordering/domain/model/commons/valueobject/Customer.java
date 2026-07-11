package br.com.ipet.ordering.domain.model.commons.valueobject;

import br.com.ipet.ordering.domain.model.FieldValidator;
import br.com.ipet.ordering.domain.model.commons.document.Document;
import lombok.Builder;

@Builder
public record Customer(FullName fullName, Document document,
                       Phone phone, Email email, Address address) {

    public Customer {
        FieldValidator.requiresNonNull("fullName", fullName);
        FieldValidator.requiresNonNull("document", document);
        FieldValidator.requiresNonNull("celPhone", phone);
        FieldValidator.requiresNonNull("email", email);
        FieldValidator.requiresNonNull("address", address);
    }
}
