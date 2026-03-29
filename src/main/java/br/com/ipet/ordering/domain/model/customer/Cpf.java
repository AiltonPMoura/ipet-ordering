package br.com.ipet.ordering.domain.model.customer;

import br.com.ipet.ordering.domain.model.FieldValidator;
import br.com.ipet.ordering.domain.model.commons.Document;
import br.com.ipet.ordering.domain.model.commons.DocumentIsNotValidException;

public record Cpf(String value) implements Document {

    public Cpf {
        FieldValidator.requiresNonNull("document value", value);
        this.isValid(value);
    }

    @Override
    public void isValid(String cpf) throws DocumentIsNotValidException {
        if (cpf.length() != 11)
            throw new DocumentIsNotValidException("");
    }
}
