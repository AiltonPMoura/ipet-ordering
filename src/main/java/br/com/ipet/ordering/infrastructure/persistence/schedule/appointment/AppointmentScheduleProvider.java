package br.com.ipet.ordering.infrastructure.persistence.schedule.appointment;

import br.com.ipet.ordering.domain.model.commons.valueobject.Email;
import br.com.ipet.ordering.domain.model.company.CompanyId;
import br.com.ipet.ordering.domain.model.customer.CustomerId;
import br.com.ipet.ordering.domain.model.schedule.ScheduleId;
import br.com.ipet.ordering.domain.model.schedule.appointment.AppointmentSchedule;
import br.com.ipet.ordering.domain.model.schedule.appointment.AppointmentSchedules;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AppointmentScheduleProvider implements AppointmentSchedules {

    private final AppointmentSchedulePersistenceRepository appointmentScheduleRepository;
    private final AppointmentScheduleMapper mapper;
    private final AppointmentSchedulePersistenceMapper persistenceMapper;

    @Override
    public Optional<AppointmentSchedule> ofId(ScheduleId id) {
        return appointmentScheduleRepository.findById(id.value()).map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean exists(ScheduleId id) {
        return appointmentScheduleRepository.existsById(id.value());
    }

    @Override
    @Transactional
    public void add(AppointmentSchedule appointmentSchedule) {
        appointmentScheduleRepository.findById(appointmentSchedule.id().value())
                .ifPresentOrElse(
                        appointmentSchedulePersistence -> this.update(appointmentSchedulePersistence, appointmentSchedule),
                        () -> this.insert(appointmentSchedule)
                );

        appointmentSchedule.clearDomainEvents();
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return appointmentScheduleRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByCompany(CompanyId companyId) {
        return appointmentScheduleRepository.existsByCompany_Id(companyId.value());
    }

    private void insert(AppointmentSchedule appointmentSchedule) {
        var appointmentSchedulePersistence = persistenceMapper.fromDomain(appointmentSchedule);
        appointmentScheduleRepository.saveAndFlush(appointmentSchedulePersistence);
    }

    private void update(AppointmentSchedulePersistenceEntity appointmentSchedulePersistence, AppointmentSchedule appointmentSchedule) {
        appointmentSchedulePersistence = persistenceMapper.merge(appointmentSchedulePersistence, appointmentSchedule);
        appointmentScheduleRepository.saveAndFlush(appointmentSchedulePersistence);
    }
}
