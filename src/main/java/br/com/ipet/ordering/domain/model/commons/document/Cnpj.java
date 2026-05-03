package br.com.ipet.ordering.domain.model.commons.document;

import br.com.ipet.ordering.domain.model.FieldValidator;

public record Cnpj(String value) implements Document {

    public Cnpj {
        FieldValidator.requiresNonBlank("cnpj", value);

        if (!value.matches("[A-HJ-NP-Z0-9]{12}\\d{2}"))
            throw new DocumentIsNotValidException("cnpj inválido");
    }

}
