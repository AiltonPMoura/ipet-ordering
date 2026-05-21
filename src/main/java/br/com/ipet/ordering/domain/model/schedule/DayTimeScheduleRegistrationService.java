package br.com.ipet.ordering.domain.model.schedule;

import br.com.ipet.ordering.domain.model.company.Companies;
import br.com.ipet.ordering.domain.model.company.CompanyId;
import br.com.ipet.ordering.domain.model.company.CompanyNotFoundException;
import br.com.ipet.ordering.domain.model.schedule.category.ServiceSubcategory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DayTimeScheduleRegistrationService {

    private final DayTimeSchedules dayTimeSchedules;
    private final Companies companies;

    public DayTimeSchedule register(CompanyId companyId, ScheduleName name, ServiceSubcategory subcategory) {
        if (!companies.exists(companyId))
            throw new CompanyNotFoundException("");

        if (dayTimeSchedules.existsByCompanyId(companyId))
            throw new ScheduleAlreadyExistsException();

        return DayTimeSchedule.create(companyId, name, subcategory);
    }

    /*private final Bookings bookingRepository;

    public List<AvailableDateTimes> avaliableTimes(Agenda agenda) {
        FieldValidator.requiresNonNull("agenda", agenda);

        if (!agenda.isActived() && !agenda.isStantBy()) {
            throw new RuntimeException("Agenda não disponível no momento");
        }

        var bookings = bookingRepository.ofAgendaIdGreaterThanNow(agenda.id());

        var avaliableDatesTimes = agenda.avaliableDatesTimes();

        avaliableDatesTimes.forEach(availableDateTimes ->
            bookings.stream()
                    .filter(booking -> booking.date().equals(availableDateTimes.date()))
                    .forEach(booking -> {
                        var timeOfBooking = Duration.between(booking.startTime(), booking.endTime()).toMinutes();
                        var currentBookingTime = booking.startTime();

                        for (var i = 0; i < timeOfBooking; i += agenda.minuteInterval().value()) {
                            var hour = currentBookingTime.getHour();
                            var minute = currentBookingTime.getMinute();

                            availableDateTimes.availableTimes().computeIfPresent(hour, (k, v) -> {
                                v.remove((Integer) minute);
                                return v.isEmpty() ? null : v;
                            });

                            currentBookingTime = currentBookingTime.plusMinutes(1);
                        }
                    })
        );

        return avaliableDatesTimes.stream()
                .filter(availableDateTimes -> !availableDateTimes.availableTimes().isEmpty())
                .toList();
    }

    public boolean isAvaliable(Agenda agenda, LocalDate date, OffsetTime startTime, OffsetTime endTime) {
        avaliableTimes(agenda)
                .stream().anyMatch(availableDateTimes ->
                        availableDateTimes.date().equals(date)
                                && availableDateTimes.availableTimes().containsKey(startTime.getHour())
                                && availableDateTimes.availableTimes().get(startTime.getHour()).contains(startTime.getMinute())
                                && )
    }*/

}
