package br.com.ipet.ordering.domain.model.commons.document;

public interface DocumentStrategy {
    boolean match(String value);
    Document create(String value);
}
