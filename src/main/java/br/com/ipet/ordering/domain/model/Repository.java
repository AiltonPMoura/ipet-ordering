package br.com.ipet.ordering.domain.model;

import java.util.Optional;

public interface Repository<T extends AggregateRoot<I>, I> {
    Optional<T> ofId(I id);
    boolean exists(I id);
    void add(T aggregateRoot);
    int count();
}
