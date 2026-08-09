package br.com.ipet.ordering.domain.model.booking;

import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

public enum StayBookingStatus {
    DRAFT,
    PLACED,
    PAID,
    SCHEDULED,
    IN_PROGRESS,
    COMPLETED,
    CANCELED;

    private static final Map<StayBookingStatus, Set<StayBookingStatus>> STATUS_ALLOWED;

    static {
        var statusAllowed = new EnumMap<StayBookingStatus, Set<StayBookingStatus>>(StayBookingStatus.class);
        statusAllowed.put(DRAFT, EnumSet.of(PLACED));
        statusAllowed.put(PLACED, EnumSet.of(PAID, CANCELED));
        statusAllowed.put(PAID, EnumSet.of(SCHEDULED, CANCELED));
        statusAllowed.put(SCHEDULED, EnumSet.of(CANCELED, IN_PROGRESS));
        //statusAllowed.put(IN_PROGRESS, EnumSet.of(READY_FOR_DELIVERY));
        //statusAllowed.put(READY_FOR_DELIVERY, EnumSet.of(OUT_FOR_DELIVERY, DELIVERED));
        //statusAllowed.put(OUT_FOR_DELIVERY, EnumSet.of(DELIVERED));
        //statusAllowed.put(DELIVERED, EnumSet.of(COMPLETED, CANCELED));
        statusAllowed.put(COMPLETED, EnumSet.noneOf(StayBookingStatus.class));
        statusAllowed.put(CANCELED, EnumSet.noneOf(StayBookingStatus.class));
        STATUS_ALLOWED = Collections.unmodifiableMap(statusAllowed);
    }

    public boolean canChangeTo(StayBookingStatus newStatus) {
        if (newStatus == null) return false;
        return STATUS_ALLOWED.getOrDefault(this, EnumSet.noneOf(StayBookingStatus.class)).contains(newStatus);
    }

    public boolean canNotChangeTo(StayBookingStatus newStatus) {
        return !canChangeTo(newStatus);
    }

}
