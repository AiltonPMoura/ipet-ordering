package br.com.ipet.ordering.domain.model.commons.document;

import br.com.ipet.ordering.domain.model.commons.DocumentIsNotValidException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DocumentFactory {
    private final List<DocumentStrategy> strategies;

    public Document from(String value) {
        return strategies.stream()
                .filter(strategy -> strategy.match(value))
                .findFirst()
                .map(strategy -> strategy.create(value))
                .orElseThrow(() -> new DocumentIsNotValidException("documento inválido"));
    }


}
