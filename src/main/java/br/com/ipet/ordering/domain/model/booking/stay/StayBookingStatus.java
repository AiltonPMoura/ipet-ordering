package br.com.ipet.ordering.domain.model.booking.stay;

import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

public enum StayBookingStatus {
    DRAFT,
    REQUESTED,
    PAID,
    SCHEDULED,
    CHECKED_IN,
    READY_FOR_CHECKOUT,
    CHECKED_OUT,
    COMPLETED,
    CANCELED,
    REFUNDED;
    //NO_SHOW;

    private static final Map<StayBookingStatus, Set<StayBookingStatus>> ALLOWED_TRANSITIONS;

    static {
        var statusAllowed = new EnumMap<StayBookingStatus, Set<StayBookingStatus>>(StayBookingStatus.class);
        statusAllowed.put(DRAFT, EnumSet.of(REQUESTED));
        statusAllowed.put(REQUESTED, EnumSet.of(PAID, CANCELED));
        statusAllowed.put(PAID, EnumSet.of(SCHEDULED, CANCELED));
        statusAllowed.put(SCHEDULED, EnumSet.of(CHECKED_IN, CANCELED));
        statusAllowed.put(CHECKED_IN, EnumSet.of(READY_FOR_CHECKOUT, CANCELED));
        statusAllowed.put(READY_FOR_CHECKOUT, EnumSet.of(CHECKED_OUT));
        statusAllowed.put(CHECKED_OUT, EnumSet.of(COMPLETED, CANCELED));
        statusAllowed.put(COMPLETED, EnumSet.noneOf(StayBookingStatus.class));
        statusAllowed.put(CANCELED, EnumSet.of(REFUNDED));
        statusAllowed.put(REFUNDED, EnumSet.noneOf(StayBookingStatus.class));
        ALLOWED_TRANSITIONS = Collections.unmodifiableMap(statusAllowed);
    }

    public boolean canChangeTo(StayBookingStatus newStatus) {
        if (newStatus == null) return false;
        return ALLOWED_TRANSITIONS.getOrDefault(this, EnumSet.noneOf(StayBookingStatus.class)).contains(newStatus);
    }

    public boolean canNotChangeTo(StayBookingStatus newStatus) {
        return !canChangeTo(newStatus);
    }

}
