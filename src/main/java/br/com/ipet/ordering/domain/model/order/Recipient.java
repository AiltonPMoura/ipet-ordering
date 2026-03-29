package br.com.ipet.ordering.domain.model.order;

import br.com.ipet.ordering.domain.model.FieldValidator;
import br.com.ipet.ordering.domain.model.commons.Document;
import br.com.ipet.ordering.domain.model.commons.valueobject.CelPhone;
import br.com.ipet.ordering.domain.model.commons.valueobject.FullName;
import lombok.Builder;

@Builder
public record Recipient(FullName fullName, Document document, CelPhone celPhone) {

    public Recipient {
        FieldValidator.requiresNonNull("fullName", fullName);
        FieldValidator.requiresNonNull("document", document);
        FieldValidator.requiresNonNull("celPhone", celPhone);
    }

}
