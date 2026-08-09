package br.com.ipet.ordering.domain.model.order;

import br.com.ipet.ordering.domain.model.FieldValidator;

import java.util.Arrays;
import java.util.List;

public enum OrderStatus {
    DRAFT,
    PLACED(DRAFT),
    PAID(PLACED),
    //PREPARING --Sugestão de status intermediario entre PAID e READY_FOR_DELIVERY, para informar ao cliente que o pedido está sendo preparado
    READY_FOR_DELIVERY(PAID),
    OUT_FOR_DELIVERY(READY_FOR_DELIVERY),
    DELIVERED(PAID, READY_FOR_DELIVERY, OUT_FOR_DELIVERY),
    COMPLETED(DELIVERED),
    CANCELED(PLACED, PAID, READY_FOR_DELIVERY, OUT_FOR_DELIVERY, DELIVERED);
    //REFUNDED -- Sugestão de status para casos de estorno

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
