package br.com.ipet.ordering.domain.model.transport;

import br.com.ipet.ordering.domain.model.FieldValidator;
import br.com.ipet.ordering.domain.model.commons.document.Document;
import br.com.ipet.ordering.domain.model.commons.valueobject.FullName;
import lombok.Builder;

@Builder
public record Driver(FullName fullName, Document document) {

    public Driver {
        FieldValidator.requiresNonNull("fullName", fullName);
        FieldValidator.requiresNonNull("document", document);
    }

}
