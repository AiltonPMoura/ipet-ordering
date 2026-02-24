package br.com.ipet.ordering.infrastructure.persistence.provider;

import br.com.ipet.ordering.domain.model.schedule.Schedule;
import br.com.ipet.ordering.domain.model.schedule.Schedules;
import br.com.ipet.ordering.domain.model.schedule.ScheduleId;
import br.com.ipet.ordering.domain.model.customer.CompanyId;
import br.com.ipet.ordering.infrastructure.persistence.repository.AgendaPersistenceEntityRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class AgendaPersistenceProvider implements Schedules {

    private final AgendaPersistenceEntityRepository repository;

    @Override
    public Optional<Schedule> ofId(ScheduleId id) {
        return Optional.empty();
    }

    @Override
    public boolean exists(ScheduleId id) {
        return false;
    }

    @Override
    public void add(Schedule aggregateRoot) {

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
