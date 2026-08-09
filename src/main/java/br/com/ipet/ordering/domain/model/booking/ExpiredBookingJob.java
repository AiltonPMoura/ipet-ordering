package br.com.ipet.ordering.domain.model.booking;

import br.com.ipet.ordering.domain.model.booking.appointment.AppointmentBooking;
import br.com.ipet.ordering.domain.model.booking.appointment.AppointmentBookingStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class ExpiredBookingJob {

    private final BookingRepository repository;

    // Roda de 1 em 1 minuto, por exemplo
    @Scheduled(cron = "0 * * * * *")
    @Transactional
    public void cancelExpiredRequests() {
        // 1. Calcula o limite de tempo
        LocalDateTime timeoutLimit = LocalDateTime.now().minusMinutes(15);

        // 2. Busca quem está parado em REQUESTED há mais de 15 minutos
        List<AppointmentBooking> expiredBookings = repository
                .findByStatusAndRequestedAtBefore(AppointmentBookingStatus.REQUESTED, timeoutLimit);

        // 3. Cancela todo mundo e libera a agenda
        for (AppointmentBooking booking : expiredBookings) {
            // Usa o método que você já criou no seu Aggregate Root!
            booking.cancel("Tempo de pagamento (15 minutos) expirado.");
            repository.save(booking);
        }
    }

}
