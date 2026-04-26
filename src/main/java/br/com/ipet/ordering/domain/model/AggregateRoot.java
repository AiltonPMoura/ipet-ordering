package br.com.ipet.ordering.domain.model;

public interface AggregateRoot<I> extends DomainEventSource {
    I id();
}
