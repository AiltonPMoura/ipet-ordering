package br.com.ipet.ordering.infrastructure.persistence.provider;

import br.com.ipet.ordering.domain.model.entity.Agenda;
import br.com.ipet.ordering.domain.model.repository.Agendas;
import br.com.ipet.ordering.domain.model.valueobject.AgendaId;
import br.com.ipet.ordering.infrastructure.persistence.repository.AgendaPersistenceEntityRepository;

import java.util.Optional;

public class AgendaPersistenceProvider implements Agendas {

    private AgendaPersistenceEntityRepository repository;

    @Override
    public Optional<Agenda> ofId(AgendaId id) {
        return Optional.empty();
    }

    @Override
    public boolean exists(AgendaId id) {
        return false;
    }

    @Override
    public void add(Agenda aggregateRoot) {

    }

    @Override
    public int count() {
        return 0;
    }
}
