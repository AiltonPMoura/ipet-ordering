package br.com.ipet.ordering.domain.model;

import java.util.Arrays;
import java.util.List;

public enum OrderStatus {
    DRAFT,
    PLACED(DRAFT),
    PAID(PLACED),
    READY(PAID),
    DELIVERING(READY),
    DELIVERIED(DELIVERING),
    CANCELED(DRAFT, PLACED, PAID);

    OrderStatus(OrderStatus... previousStatuses) {
        this.previousStatuses = Arrays.asList(previousStatuses);
    }

    private final List<OrderStatus> previousStatuses;

    public boolean canChange(OrderStatus newStatus) {
        var currentStatus = this;
        return newStatus.previousStatuses.contains(currentStatus);
    }

    public boolean canNotChange(OrderStatus newStatus) {
        return !canChange(newStatus);
    }
}
