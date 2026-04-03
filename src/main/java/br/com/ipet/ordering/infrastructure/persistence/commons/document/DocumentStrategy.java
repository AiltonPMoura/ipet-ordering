package br.com.ipet.ordering.infrastructure.persistence.commons.document;

import br.com.ipet.ordering.domain.model.commons.Document;

public interface DocumentStrategy {
    boolean match(String value);
    Document create(String value);
}
