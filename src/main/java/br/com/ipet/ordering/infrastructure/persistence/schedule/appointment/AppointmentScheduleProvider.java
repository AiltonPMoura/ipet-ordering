package br.com.ipet.ordering.infrastructure.persistence.schedule.appointment;

import br.com.ipet.ordering.domain.model.schedule.AppointmentSchedule;
import br.com.ipet.ordering.domain.model.schedule.AppointmentSchedules;
import br.com.ipet.ordering.domain.model.schedule.ScheduleId;
import br.com.ipet.ordering.domain.model.company.CompanyId;
import br.com.ipet.ordering.infrastructure.persistence.repository.AppointmentSchedulePersistenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AppointmentScheduleProvider implements AppointmentSchedules {

    private final AppointmentSchedulePersistenceRepository repository;

    @Override
    public Optional<AppointmentSchedule> ofId(ScheduleId id) {
        return Optional.empty();
    }

    @Override
    public boolean exists(ScheduleId id) {
        return false;
    }

    @Override
    public void add(AppointmentSchedule aggregateRoot) {

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
