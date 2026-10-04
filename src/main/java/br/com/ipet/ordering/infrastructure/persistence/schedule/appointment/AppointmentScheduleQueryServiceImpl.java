package br.com.ipet.ordering.infrastructure.persistence.schedule.appointment;

import br.com.ipet.ordering.application.schedule.appointment.query.AppointmentScheduleDetailOutput;
import br.com.ipet.ordering.application.schedule.appointment.query.AppointmentScheduleFilter;
import br.com.ipet.ordering.application.schedule.appointment.query.AppointmentScheduleQueryService;
import br.com.ipet.ordering.application.schedule.appointment.query.AppointmentScheduleSummaryOutput;
import br.com.ipet.ordering.application.util.Mapper;
import br.com.ipet.ordering.domain.model.schedule.AvailableDateTimes;
import br.com.ipet.ordering.domain.model.schedule.ScheduNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class AppointmentScheduleQueryServiceImpl implements AppointmentScheduleQueryService {

    private final AppointmentSchedulePersistenceRepository repository;
    private final Mapper mapper;

    @Override
    public AppointmentScheduleDetailOutput findByCompany(UUID companyId) {
        var appointmentSchedulePersistence = repository.findByCompany_Id(companyId).orElseThrow(() -> new ScheduNotFoundException(""));
        return mapper.convert(appointmentSchedulePersistence, AppointmentScheduleDetailOutput.class);
    }

    @Override
    public Page<AppointmentScheduleSummaryOutput> filter(AppointmentScheduleFilter filter, Pageable pageable) {
        return repository.findAll(toSpecification(filter), pageable)
                .map(appointmentSchedulePersistenceEntity ->
                        mapper.convert(appointmentSchedulePersistenceEntity, AppointmentScheduleSummaryOutput.class));
    }


    @Override
    public AvailableDateTimes findAvailableDateTimes(UUID companyId) {
        var appointmentSchedulePersistence = repository.findByCompany_Id(companyId)
                .orElseThrow(() -> new ScheduNotFoundException(""));

        var workDayPersistences = appointmentSchedulePersistence.getWorkDays();

        Set<DayOfWeek> workingDays = workDayPersistences.stream()
                .map(AppointmentWorkDayPersistenceEntity::getDayOfWeek)
                .collect(Collectors.toSet());

        // TODO: Usar appointmentSchedulePersistence.getCompany().getTimezone()
        var zoneId = ZoneId.of("America/Sao_Paulo");

        var today = LocalDate.now(zoneId);
        var currentTime = LocalTime.now(zoneId);

        var workDayHoje = workDayPersistences.stream()
                .filter(workDay -> workDay.getDayOfWeek().equals(today.getDayOfWeek()))
                .findFirst();

        // Mock: Dias de Carnaval e dias Lotados
        var quartaDeCinzas = today.plusDays(5);
        // var quintaFeira = today.plusDays(6); // <-- Quinta agora está LIVRE!
        var sextaFeira = today.plusDays(7);

        // MOCK: Verifica se a data está 100% lotada (Apenas Quarta e Sexta)
        Predicate<LocalDate> isFullyBooked = date -> date.equals(quartaDeCinzas) || date.equals(sextaFeira);

        // 1. Capacidade de triagem HOJE (Sexta-feira 20h)
        boolean canApproveToday = workDayHoje.isPresent()
                && !appointmentSchedulePersistence.getLockedDates().contains(today)
                && !isFullyBooked.test(today)
                && currentTime.isBefore(workDayHoje.get().getEndTime());

        // Como já é 20h, canApproveToday = false. Precisamos pular 1 dia útil REAL.
        long workingDaysToSkip = canApproveToday ? 0 : 1;

        // 2. A Janela Fixa processando a Tempestade Perfeita
        var avaliableDates = Stream.iterate(today.plusDays(1), date -> date.plusDays(1))
                .limit(15) // Olha 15 dias para frente
                .filter(date -> workingDays.contains(date.getDayOfWeek())) // Elimina Finais de Semana
                .filter(date -> !appointmentSchedulePersistence.getLockedDates().contains(date)) // Elimina Feriados (Carnaval)
                .filter(date -> !isFullyBooked.test(date)) // NOVO: Elimina os dias lotados!
                .skip(workingDaysToSkip) // Pula 1 dia útil real para a equipe respirar
                .toList();


        return avaliableDates.stream()
                .map(date -> new AvailableDateTimes(date, null))
                .toList();
    }

    private Specification<AppointmentSchedulePersistenceEntity> toSpecification(AppointmentScheduleFilter filter) {
        return null;/*firstNameLike(filter.getFirstName())
                .or(lastNameLike(filter.getLastName()))
                .or(emailLike(filter.getEmail()));*/
    }

}
