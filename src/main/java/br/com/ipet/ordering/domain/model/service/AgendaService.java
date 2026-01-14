package br.com.ipet.ordering.domain.model.service;

import br.com.ipet.ordering.domain.model.entity.Agenda;
import br.com.ipet.ordering.domain.model.repository.Bookings;
import br.com.ipet.ordering.domain.model.util.FieldValidator;
import br.com.ipet.ordering.domain.model.valueobject.AvailableDateTimes;
import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.time.Period;
import java.util.List;

@RequiredArgsConstructor
public class AgendaService {

    private final Bookings bookingRepository;

    public List<AvailableDateTimes> avaliableTimes(Agenda agenda) {
        FieldValidator.requiresNonNull("agenda", agenda);

        if (!agenda.isActived() && !agenda.isStantBy()) {
            throw new RuntimeException("Agenda não disponível no momento");
        }

        var bookings = bookingRepository.ofAgendaId(agenda.id());

        var avaliableDatesTimes = agenda.avaliableDatesTimes();

        avaliableDatesTimes.forEach(availableDateTimes ->
            bookings.stream()
                    .filter(booking -> booking.date().equals(availableDateTimes.date()))
                    .forEach(booking -> {
                        var timeOfBooking = Duration.between(booking.startTime(), booking.endTime()).toMinutes();
                        var currentBookingTime = booking.startTime();

                        for (var i = 0; i < timeOfBooking; i++) {
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

}
