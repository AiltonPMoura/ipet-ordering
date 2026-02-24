package br.com.ipet.ordering.infrastructure.persistence.provider;

import br.com.ipet.ordering.domain.model.agenda.Agenda;
import br.com.ipet.ordering.domain.model.agenda.Agendas;
import br.com.ipet.ordering.domain.model.agenda.AgendaId;
import br.com.ipet.ordering.domain.model.customer.CompanyId;
import br.com.ipet.ordering.infrastructure.persistence.repository.AgendaPersistenceEntityRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class AgendaPersistenceProvider implements Agendas {

    private final AgendaPersistenceEntityRepository repository;

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

    @Override
    public boolean existsByCompanyId(CompanyId companyId) {
        return repository.existsByCompanyId(companyId.value());
    }
}
