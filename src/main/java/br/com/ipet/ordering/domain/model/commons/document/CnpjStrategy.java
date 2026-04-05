package br.com.ipet.ordering.domain.model.commons.document;

public class CnpjStrategy implements DocumentStrategy{
    @Override
    public boolean match(String value) {
        return value.matches("[A-HJ-NP-Z0-9]{12}\\d{2}");
    }

    @Override
    public Document create(String value) {
        return new Cnpj(value);
    }
}
