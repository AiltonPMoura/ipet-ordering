package br.com.ipet.ordering.application.util;

public interface Mapper {
    <T> T convert(Object object, Class<T> destination);
}
