package br.com.ipet.ordering.infrastructure.persistence.schedule.appointment;

import br.com.ipet.ordering.application.schedule.appointment.query.AppointmentScheduleDetailOutput;
import br.com.ipet.ordering.application.schedule.appointment.query.AppointmentScheduleFilter;
import br.com.ipet.ordering.application.schedule.appointment.query.AppointmentScheduleQueryService;
import br.com.ipet.ordering.application.schedule.appointment.query.AppointmentScheduleSummaryOutput;
import br.com.ipet.ordering.application.util.Mapper;
import br.com.ipet.ordering.domain.model.schedule.AvailableDateTimes;
import br.com.ipet.ordering.domain.model.schedule.AvailableDay;
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
        var schedulePersistence = repository.findByCompany_Id(companyId)
                .orElseThrow(() -> new ScheduNotFoundException(""));

        var workDayPersistences = schedulePersistence.getWorkDays();

        // TODO: Usar schedulePersistence.getCompany().getTimezone()
        var zoneId = ZoneId.of("America/Sao_Paulo");
        var today = LocalDate.now(zoneId);
        var currentTime = LocalTime.now(zoneId);

        Set<DayOfWeek> workingDays = workDayPersistences.stream()
                .map(AppointmentWorkDayPersistenceEntity::getDayOfWeek)
                .collect(Collectors.toSet());

        var workDayHoje = workDayPersistences.stream()
                .filter(workDay -> workDay.getDayOfWeek().equals(today.getDayOfWeek()))
                .findFirst();

        // Mock: Dias de Carnaval e dias Lotados
        //var quartaDeCinzas = today.plusDays(5);
        // var quintaFeira = today.plusDays(6); // <-- Quinta agora está LIVRE!
        //var sextaFeira = today.plusDays(7);
        // MOCK: Verifica se a data está 100% lotada (Apenas Quarta e Sexta)
        //Predicate<LocalDate> isFullyBooked = date -> date.equals(quartaDeCinzas) || date.equals(sextaFeira);

        boolean canApproveToday = workDayHoje.isPresent()
                && currentTime.isBefore(workDayHoje.get().getEndTime())
                && !schedulePersistence.getLockedDates().contains(today);
                //&& !isFullyBooked.test(today);

        long workingDaysToSkip = canApproveToday ? 0 : 1;

        var availableDates = Stream.iterate(today.plusDays(1), date -> date.plusDays(1))
                .limit(15)
                .filter(date -> workingDays.contains(date.getDayOfWeek()))
                .filter(date -> !schedulePersistence.getLockedDates().contains(date))
                //.filter(date -> !isFullyBooked.test(date))// <-- Mock: Verifica se a data está 100% lotada (Apenas Quarta e Sexta)
                .skip(workingDaysToSkip)
                .toList();

        // 1. Dicionário para buscar o horário de trabalho de forma rápida (O(1))
        var workDayMap = workDayPersistences.stream()
                .collect(Collectors.toMap(
                        AppointmentWorkDayPersistenceEntity::getDayOfWeek,
                        workDay -> workDay
                ));

        // 2. Transforma cada LocalDate em um AvailableDay com a lista de horários
        var availableDays = availableDates.stream().map(date -> {

            var workDay = workDayMap.get(date.getDayOfWeek());

            var times = Stream.iterate(workDay.getStartTime(), time -> time.plusMinutes(30))
                    .takeWhile(time -> time.plusMinutes(30).isBefore(workDay.getEndTime())
                            || time.plusMinutes(30).equals(workDay.getEndTime()))

                    // NOVO: Filtra os horários que caem dentro de alguma pausa (almoço)
                    .filter(time -> {
                        LocalTime slotStart = time;
                        LocalTime slotEnd = time.plusMinutes(30);

                        // Supondo que workDay.getBreaks() retorne os intervalos de pausa do dia
                        boolean isDuringBreak = workDay.getLockedTimes().stream().anyMatch(pause ->
                                // O bloco conflita se começar ANTES da pausa terminar
                                // E terminar DEPOIS da pausa começar
                                slotStart.isBefore(pause.getEndTime()) && slotEnd.isAfter(pause.getStartTime())
                        );

                        // Mantém na lista apenas se NÃO estiver no horário de pausa
                        return !isDuringBreak;
                    })
                    .toList();

            return new AvailableDay(date, times);

        }).toList();

        return new AvailableDateTimes(availableDates.getFirst(), availableDates.getLast(), availableDays);
    }

    private Specification<AppointmentSchedulePersistenceEntity> toSpecification(AppointmentScheduleFilter filter) {
        return null;/*firstNameLike(filter.getFirstName())
                .or(lastNameLike(filter.getLastName()))
                .or(emailLike(filter.getEmail()));*/
    }

}
