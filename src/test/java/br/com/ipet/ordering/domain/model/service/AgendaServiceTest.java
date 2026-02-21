package br.com.ipet.ordering.domain.model.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AgendaServiceTest {

    /*@InjectMocks
    private AgendaService agendaService;

    @Test
    public void givenAvaliableDatesDays_whenCreateCalendar_thenReturnAvaliableTimesDatesTimesStartingTomorrow() {

        Agenda agenda = Agenda.createNew().companyId(new CompanyId()).name(new AgendaName("teste")).build();
        agenda.changeBookingBy(new BookingWindow(7));
        agenda.addWorkingDay(new DayTime(DayOfWeek.THURSDAY, OffsetTime.now(), OffsetTime.now().plusHours(1)));
        agenda.addWorkingDay(new DayTime(DayOfWeek.WEDNESDAY, OffsetTime.now(), OffsetTime.now().plusHours(1)));
        agenda.addWorkingDay(new DayTime(DayOfWeek.FRIDAY, OffsetTime.now(), OffsetTime.now().plusHours(1)));
        agenda.addStandByDates(new StandByDates(LocalDate.now().plusDays(4), LocalDate.now().plusDays(6)));

        agenda.active();

        var result = agendaService.avaliableTimes(agenda);

        Assertions.assertThat(result).isNotEmpty();
        Assertions.assertThat(result.getFirst()).isEqualTo(LocalDate.now().plusDays(7));
        Assertions.assertThat(result.getLast()).isEqualTo(LocalDate.now().plusDays(7));
        Assertions.assertThat(result).hasSize(1);

    }*/

}