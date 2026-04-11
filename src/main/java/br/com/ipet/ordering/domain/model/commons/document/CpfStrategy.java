package br.com.ipet.ordering.domain.model.commons.document;

import org.springframework.stereotype.Component;

@Component
public class CpfStrategy implements DocumentStrategy {

    @Override
    public boolean match(String value) {
        return value.matches("\\d{11}");
    }

    @Override
    public Document create(String value) {
        return new Cpf(value);
    }
}
