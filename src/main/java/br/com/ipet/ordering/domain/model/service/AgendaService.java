package br.com.ipet.ordering.domain.model.service;

import br.com.ipet.ordering.Booking;
import br.com.ipet.ordering.domain.model.entity.Agenda;
import br.com.ipet.ordering.domain.model.repository.Bookings;
import br.com.ipet.ordering.domain.model.util.FieldValidator;
import br.com.ipet.ordering.domain.model.valueobject.AvailableDateTimes;
import lombok.RequiredArgsConstructor;

import java.time.Period;
import java.util.List;

@RequiredArgsConstructor
public class AgendaService {

    private final Bookings bookingRepository;

    public List<AvailableDateTimes> calendar(Agenda agenda) {
        FieldValidator.requiresNonNull("agenda", agenda);

        if (!agenda.isActived() && !agenda.isStantBy()) {
            throw new RuntimeException("Agenda não disponível no momento");
        }

        var bookings = bookingRepository.ofAgendaId(agenda.id());



        var avaliableDatesTimes = agenda.avaliableDatesTimes();

        avaliableDatesTimes.stream().map(availableDateTimes -> {
            bookings.stream()
                    .filter(booking -> booking.date().equals(availableDateTimes.date()))
                    .findFirst()
                    .ifPresent(booking -> );

            avaliableDatesTimes
        })

        return avaliableDatesTimes;

    }

    public void subtractBookingTimes(AvailableDateTimes availableDateTimes, Book ing booking) {
        Period.
        var bookingTime = booking.startTime()
        var times = availableDateTimes.availableTimes().get(booking.)
    }



}
