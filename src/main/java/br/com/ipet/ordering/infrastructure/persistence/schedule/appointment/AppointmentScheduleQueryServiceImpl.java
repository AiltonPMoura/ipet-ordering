package br.com.ipet.ordering.infrastructure.persistence.schedule.appointment;

import br.com.ipet.ordering.application.schedule.query.AppointmentScheduleDetailOutput;
import br.com.ipet.ordering.application.schedule.query.AppointmentScheduleFilter;
import br.com.ipet.ordering.application.schedule.query.AppointmentScheduleQueryService;
import br.com.ipet.ordering.application.schedule.query.AppointmentScheduleSummaryOutput;
import br.com.ipet.ordering.application.util.Mapper;
import br.com.ipet.ordering.domain.model.schedule.AvailableDateTimes;
import br.com.ipet.ordering.domain.model.schedule.ScheduNotFoundException;
import br.com.ipet.ordering.infrastructure.persistence.entity.AppointmentSchedulePersistenceEntity;
import br.com.ipet.ordering.infrastructure.persistence.entity.WorkingDayPersistenceEntity;
import br.com.ipet.ordering.infrastructure.persistence.repository.AppointmentSchedulePersistenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class AppointmentScheduleQueryServiceImpl implements AppointmentScheduleQueryService {

    private final AppointmentSchedulePersistenceRepository repository;
    private final Mapper mapper;

    @Override
    public AppointmentScheduleDetailOutput findById(UUID scheduleId) {
        var dayTimeSchedule = repository.findById(scheduleId).orElseThrow(() -> new ScheduNotFoundException(""));
        return mapper.convert(dayTimeSchedule, AppointmentScheduleDetailOutput.class);
    }

    @Override
    public Page<AppointmentScheduleSummaryOutput> filter(AppointmentScheduleFilter filter, Pageable pageable) {
        return repository.findAll(toSpecification(filter), pageable)
                .map(appointmentSchedulePersistenceEntity ->
                        mapper.convert(appointmentSchedulePersistenceEntity, AppointmentScheduleSummaryOutput.class));
    }

    @Override
    public List<AvailableDateTimes> findAvailableDateTimes(UUID scheduleId) {

        var dayTimeSchedule = repository.findById(scheduleId).orElseThrow(() -> new ScheduNotFoundException(""));
        Set<DayOfWeek> workingDays = dayTimeSchedule.getWorkingDays().stream()
                .map(WorkingDayPersistenceEntity::getDayOfWeek) // Chama o getter da sua propriedade
                .collect(Collectors.toSet());

        LocalDate today = LocalDate.now(ZoneOffset.UTC);

        var avaliableDates = Stream.iterate(today, date -> date.plusDays(1))
                .limit(15)
                .filter(date -> workingDays.contains(date.getDayOfWeek()))
                .toList();


        return List.of();
    }

    private Specification<AppointmentSchedulePersistenceEntity> toSpecification(AppointmentScheduleFilter filter) {
        return null;/*firstNameLike(filter.getFirstName())
                .or(lastNameLike(filter.getLastName()))
                .or(emailLike(filter.getEmail()));*/
    }

}
