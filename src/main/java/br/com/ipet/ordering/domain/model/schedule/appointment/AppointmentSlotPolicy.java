package br.com.ipet.ordering.domain.model.schedule.appointment;

import br.com.ipet.ordering.domain.model.FieldValidator;

import java.time.LocalTime;

public final class AppointmentSlotPolicy {

    public static final int INCREMENT_MINUTES = 30;

    private AppointmentSlotPolicy() {
    }

    public static void verifyStartAligned(LocalTime startTime) {
        FieldValidator.requiresNonNull("startTime", startTime);

        if (startTime.getMinute() % INCREMENT_MINUTES != 0)
            throw new InvalidAppointmentSlotException();
    }
}
