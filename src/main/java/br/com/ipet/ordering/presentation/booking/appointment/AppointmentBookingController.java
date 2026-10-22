package br.com.ipet.ordering.presentation.booking.appointment;

import br.com.ipet.ordering.application.booking.appointment.management.AppointmentBookingInput;
import br.com.ipet.ordering.application.booking.appointment.management.AppointmentBookingManagementApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/v1/appointment-bookings")
@RequiredArgsConstructor
public class AppointmentBookingController {

    private final AppointmentBookingManagementApplicationService applicationService;

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody AppointmentBookingInput input) {
        var bookingId = applicationService.create(input);

        var url = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{bookingId}")
                .buildAndExpand(bookingId)
                .toUri();

        return ResponseEntity.created(url).build();
    }

}
