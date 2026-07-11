package br.com.ipet.ordering.domain.model.booking;

import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

public enum TimeSlotBookingStatus {
    DRAFT,
    PLACED,
    PAID,
    SCHEDULED,
    IN_PROGRESS,// Como Iniciar automaticamente
    READY_FOR_DELIVERY,
    OUT_FOR_DELIVERY,
    DELIVERED,
    COMPLETED,
    CANCELED;

    private static final Map<TimeSlotBookingStatus, Set<TimeSlotBookingStatus>> STATUS_ALLOWED;

    static {
        var statusAllowed = new EnumMap<TimeSlotBookingStatus, Set<TimeSlotBookingStatus>>(TimeSlotBookingStatus.class);
        statusAllowed.put(DRAFT, EnumSet.of(PLACED));
        statusAllowed.put(PLACED, EnumSet.of(PAID, CANCELED));
        statusAllowed.put(PAID, EnumSet.of(SCHEDULED, CANCELED));
        statusAllowed.put(SCHEDULED, EnumSet.of(CANCELED, IN_PROGRESS));
        statusAllowed.put(IN_PROGRESS, EnumSet.of(READY_FOR_DELIVERY));
        statusAllowed.put(READY_FOR_DELIVERY, EnumSet.of(OUT_FOR_DELIVERY, DELIVERED));
        statusAllowed.put(OUT_FOR_DELIVERY, EnumSet.of(DELIVERED));
        statusAllowed.put(DELIVERED, EnumSet.of(COMPLETED, CANCELED));
        statusAllowed.put(COMPLETED, EnumSet.noneOf(TimeSlotBookingStatus.class));
        statusAllowed.put(CANCELED, EnumSet.noneOf(TimeSlotBookingStatus.class));
        STATUS_ALLOWED = Collections.unmodifiableMap(statusAllowed);
    }

    public boolean canChangeTo(TimeSlotBookingStatus newStatus) {
        if (newStatus == null) return false;
        return STATUS_ALLOWED.getOrDefault(this, EnumSet.noneOf(TimeSlotBookingStatus.class)).contains(newStatus);
    }

    public boolean canNotChangeTo(TimeSlotBookingStatus newStatus) {
        return !canChangeTo(newStatus);
    }

}
