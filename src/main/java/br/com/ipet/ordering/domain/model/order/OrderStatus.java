package br.com.ipet.ordering.domain.model.order;

import br.com.ipet.ordering.domain.model.FieldValidator;

import java.util.Arrays;
import java.util.List;

public enum OrderStatus {
    DRAFT,
    PLACED(DRAFT),
    PAID(PLACED),
    READY(PAID),
    DELIVERING(READY),
    COMPLETED(DELIVERING),
    CANCELED(DRAFT, PLACED, PAID, READY, DELIVERING);

    OrderStatus(OrderStatus... previousStatuses) {
        this.previousStatuses = Arrays.asList(previousStatuses);
    }

    private final List<OrderStatus> previousStatuses;

    public boolean canChangeTo(OrderStatus newStatus) {
        FieldValidator.requiresNonNull("newStatus", newStatus);

        var currentStatus = this;
        return newStatus.previousStatuses.contains(currentStatus);
    }

    public boolean canNotChangeTo(OrderStatus newStatus) {
        return !canChangeTo(newStatus);
    }
}
