package br.com.ipet.ordering.application.order.management;

import br.com.ipet.ordering.domain.model.order.Order;
import br.com.ipet.ordering.domain.model.order.OrderId;
import br.com.ipet.ordering.domain.model.order.OrderNotFoundException;
import br.com.ipet.ordering.domain.model.order.Orders;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderManagementApplicationService {

    private final Orders orders;

    public void markAsPaid(UUID orderId) {
        var order = this.findOrderById(orderId);
        order.markAsPaid();
        orders.add(order);
    }

    public void readyForDelivery(UUID orderId) {
        var order = this.findOrderById(orderId);
        order.readyForDelivery();
        orders.add(order);
    }

    public void outForDelivery(UUID orderId) {
        var order = this.findOrderById(orderId);
        order.outForDelivery();
        orders.add(order);
    }

    public void delivered(UUID orderId) {
        var order = this.findOrderById(orderId);
        order.deliver();
        orders.add(order);
    }

    public void complete(UUID orderId) {
        var order = this.findOrderById(orderId);
        order.complete();
        orders.add(order);
    }

    public void cancel(UUID orderId) {
        var order = this.findOrderById(orderId);
        order.cancel();
        orders.add(order);
    }

    private Order findOrderById(UUID orderId) {
        return orders.ofId(new OrderId(orderId))
                .orElseThrow(() -> new OrderNotFoundException(""));
    }

}
