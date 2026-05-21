package br.com.ipet.ordering.infrastructure.persistence.schedule;

import br.com.ipet.ordering.domain.model.schedule.DayTimeSchedule;
import br.com.ipet.ordering.domain.model.schedule.DayTimeSchedules;
import br.com.ipet.ordering.domain.model.schedule.ScheduleId;
import br.com.ipet.ordering.domain.model.company.CompanyId;
import br.com.ipet.ordering.infrastructure.persistence.repository.DayTimeSchedulePersistenceRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class DayTimeScheduleProvider implements DayTimeSchedules {

    private final DayTimeSchedulePersistenceRepository repository;

    @Override
    public Optional<DayTimeSchedule> ofId(ScheduleId id) {
        return Optional.empty();
    }

    @Override
    public boolean exists(ScheduleId id) {
        return false;
    }

    @Override
    public void add(DayTimeSchedule aggregateRoot) {

    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public boolean existsByCompanyId(CompanyId companyId) {
        return repository.existsByCompanyId(companyId.value());
    }
}
