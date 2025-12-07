package br.com.ipet.ordering.infrastructure.persistence.mapper;

import br.com.ipet.ordering.domain.model.entity.Order;
import br.com.ipet.ordering.infrastructure.persistence.entity.OrderPersistenceEntity;
import org.springframework.stereotype.Component;

@Component
public class OrderPersistenceEntityMapper {

    public OrderPersistenceEntity fromDomain(Order order) {
        return merge(new OrderPersistenceEntity(), order);
    }

    private OrderPersistenceEntity merge(OrderPersistenceEntity orderPersistenceEntity, Order order) {
        orderPersistenceEntity.setId(order.id().value());
        orderPersistenceEntity.setCustomerId(order.custumerId().value());
        orderPersistenceEntity.setCompanyId(order.ccompanyId().value());
        orderPersistenceEntity.setTotalAmount(order.totalAmount().value());
        orderPersistenceEntity.setTotalItems(order.totalItems().value());
        orderPersistenceEntity.setPaymentMethod(order.paymentMethod().name());
        orderPersistenceEntity.setStatus(order.status().name());
        orderPersistenceEntity.setPlacedAt(order.placedAt());
        orderPersistenceEntity.setPaidAt(order.paidAt());
        orderPersistenceEntity.setReadyAt(order.readyAt());
        orderPersistenceEntity.setDeliveringAt(order.deliveringAt());
        orderPersistenceEntity.setDeliveryAt(order.deliveryAt());
        orderPersistenceEntity.setCancelAt(order.cancelAt());

        return orderPersistenceEntity;
    }

}
