package br.com.ipet.ordering.infrastructure.persistence.commons.document;

import br.com.ipet.ordering.domain.model.commons.Document;
import br.com.ipet.ordering.domain.model.customer.Cpf;

public class CpfStrategy implements DocumentStrategy{
    @Override
    public boolean match(String value) {
        return value.matches("\\d{11}");
    }

    @Override
    public Document create(String value) {
        return new Cpf(value);
    }
}
