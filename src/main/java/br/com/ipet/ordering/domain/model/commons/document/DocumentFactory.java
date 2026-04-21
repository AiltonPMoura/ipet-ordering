package br.com.ipet.ordering.domain.model.commons.document;

import br.com.ipet.ordering.domain.model.FieldValidator;
import br.com.ipet.ordering.domain.model.commons.DocumentIsNotValidException;

public final class DocumentFactory {

    private DocumentFactory() {}

    public static Document from(String value) {
        FieldValidator.requiresNonBlank("document", value);
        value = value.trim().toUpperCase();

        if (value.length() == 11)
            return new Cpf(value);

        if (value.length() == 14)
            return new Cnpj(value);

        throw new DocumentIsNotValidException("documento inválido");
    }

}
