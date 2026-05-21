package br.com.ipet.ordering.infrastructure.persistence.schedule;

import br.com.ipet.ordering.application.schedule.query.DayTimeScheduleDetailOutput;
import br.com.ipet.ordering.application.schedule.query.DayTimeScheduleFilter;
import br.com.ipet.ordering.application.schedule.query.DayTimeScheduleQueryService;
import br.com.ipet.ordering.application.schedule.query.DayTimeScheduleSummaryOutput;
import br.com.ipet.ordering.application.util.Mapper;
import br.com.ipet.ordering.domain.model.schedule.ScheduNotFoundException;
import br.com.ipet.ordering.infrastructure.persistence.entity.DayTimeSchedulePersistenceEntity;
import br.com.ipet.ordering.infrastructure.persistence.repository.DayTimeSchedulePersistenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

@RequiredArgsConstructor
public class DayTimeScheduleQueryServiceImpl implements DayTimeScheduleQueryService, ScheduleQueryService {

    private final DayTimeSchedulePersistenceRepository repository;
    private final Mapper mapper;

    @Override
    public DayTimeScheduleDetailOutput findById(UUID scheduleId) {
        var dayTimeSchedule = repository.findById(scheduleId).orElseThrow(() -> new ScheduNotFoundException(""));
        return mapper.convert(dayTimeSchedule, DayTimeScheduleDetailOutput.class);
    }

    @Override
    public Page<DayTimeScheduleSummaryOutput> filter(DayTimeScheduleFilter filter, Pageable pageable) {
        return repository.findAll(toSpecification(filter), pageable)
                .map(dayTimeSchedulePersistenceEntity ->
                        mapper.convert(dayTimeSchedulePersistenceEntity, DayTimeScheduleSummaryOutput.class));
    }

    private Specification<DayTimeSchedulePersistenceEntity> toSpecification(DayTimeScheduleFilter filter) {
        return null;/*firstNameLike(filter.getFirstName())
                .or(lastNameLike(filter.getLastName()))
                .or(emailLike(filter.getEmail()));*/
    }

}
