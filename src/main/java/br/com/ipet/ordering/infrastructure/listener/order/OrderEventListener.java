package br.com.ipet.ordering.infrastructure.listener.order;

import br.com.ipet.ordering.domain.model.order.OrderCanceledEvent;
import br.com.ipet.ordering.domain.model.order.OrderDeliveredEvent;
import br.com.ipet.ordering.domain.model.order.OrderOutForDeliveredEvent;
import br.com.ipet.ordering.domain.model.order.OrderPaidEvent;
import br.com.ipet.ordering.domain.model.order.OrderPlacedEvent;
import br.com.ipet.ordering.domain.model.order.OrderReadyEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OrderEventListener {

    @EventListener
    public void onPlaced(OrderPlacedEvent event) {
        log.info("placed");
    }

    @EventListener
    public void onReady(OrderReadyEvent event) {
        log.info("ready");
    }

    @EventListener
    public void onPaid(OrderPaidEvent event) {
        log.info("paid");
    }

    @EventListener
    public void onOutForDelivery(OrderOutForDeliveredEvent event) {
        log.info("out for delivery");
    }

    @EventListener
    public void onDelivered(OrderDeliveredEvent event) {
        log.info("delivered");
    }

    @EventListener
    public void onCancel(OrderCanceledEvent event) {
        log.info("canceled");
    }

}
