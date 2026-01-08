package br.com.ipet.ordering.domain.model.service;

import br.com.ipet.ordering.domain.model.entity.Agenda;
import br.com.ipet.ordering.domain.model.valueobject.AgendaName;
import br.com.ipet.ordering.domain.model.valueobject.CompanyId;
import br.com.ipet.ordering.domain.model.valueobject.DayTime;
import br.com.ipet.ordering.domain.model.valueobject.StandByDates;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.OffsetTime;

@ExtendWith(MockitoExtension.class)
class AgendaServiceTest {

    @InjectMocks
    private AgendaService agendaService;

    @Test
    public void givenBookingDays_whenCreateCalendar_thenReturnCalendarDatesStartingTomorrow() {

        Agenda agenda = Agenda.createNew().companyId(new CompanyId()).name(new AgendaName("teste")).build();
        //agenda.changeBookingBy(15);
        agenda.addWorkingDay(new DayTime(DayOfWeek.THURSDAY, OffsetTime.now(), OffsetTime.now().plusHours(1)));
        agenda.addWorkingDay(new DayTime(DayOfWeek.WEDNESDAY, OffsetTime.now(), OffsetTime.now().plusHours(1)));
        agenda.addWorkingDay(new DayTime(DayOfWeek.TUESDAY, OffsetTime.now(), OffsetTime.now().plusHours(1)));
        agenda.addWorkingDay(new DayTime(DayOfWeek.SATURDAY, OffsetTime.now(), OffsetTime.now().plusHours(1)));
        agenda.addStandByDates(new StandByDates(LocalDate.now().plusDays(12), LocalDate.now().plusDays(14)));

        var result = agendaService.calendar(agenda);

        Assertions.assertThat(result.getFirst()).isEqualTo(LocalDate.now().plusDays(2));
        Assertions.assertThat(result.getLast()).isEqualTo(LocalDate.now().plusDays(9));
        Assertions.assertThat(result).hasSize(5);

    }

}