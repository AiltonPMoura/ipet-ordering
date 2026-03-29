package br.com.ipet.ordering.domain.model.commons;

public interface Document {
    void isValid(String document) throws DocumentIsNotValidException;
}
