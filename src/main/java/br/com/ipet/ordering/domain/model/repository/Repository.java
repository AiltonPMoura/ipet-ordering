package br.com.ipet.ordering.domain.model.repository;

import br.com.ipet.ordering.domain.model.entity.AggregateRoot;

import java.util.Optional;

public interface Repository<T extends AggregateRoot<ID>, ID> {
    Optional<T> ofId(ID id);
    boolean exists(ID id);
    void add(T aggregateRoot);
    int count();
}
