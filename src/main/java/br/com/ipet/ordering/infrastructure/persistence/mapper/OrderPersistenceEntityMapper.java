package br.com.ipet.ordering.infrastructure.persistence.mapper;

import br.com.ipet.ordering.domain.model.order.Order;
import br.com.ipet.ordering.domain.model.order.OrderItem;
//import br.com.ipet.ordering.infrastructure.persistence.embedded.ProductEmbeddable;
import br.com.ipet.ordering.infrastructure.persistence.entity.OrderItemPersistenceEntity;
import br.com.ipet.ordering.infrastructure.persistence.entity.OrderPersistenceEntity;
import br.com.ipet.ordering.infrastructure.persistence.company.CompanyPersistenceRepository;
import br.com.ipet.ordering.infrastructure.persistence.customer.CustomerPersistenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderPersistenceEntityMapper {

    /*private final CustomerPersistenceRepository customerPersistenceRepository;
    private final CompanyPersistenceRepository companyPersistenceRepository;

    public OrderPersistenceEntity fromDomain(Order order) {
        return merge(new OrderPersistenceEntity(), order);
    }

    public OrderPersistenceEntity merge(OrderPersistenceEntity orderPersistenceEntity, Order order) {
        orderPersistenceEntity.setId(order.id().value());
        orderPersistenceEntity.setCustomer(customerPersistenceRepository.getReferenceById(order.custumerId().value()));
        orderPersistenceEntity.setCompany(companyPersistenceRepository.getReferenceById(order.ccompanyId().value()));
        orderPersistenceEntity.setTotalAmount(order.totalAmount().value());
        orderPersistenceEntity.setTotalItems(order.totalItems().value());
        orderPersistenceEntity.setPaymentMethod(order.paymentMethod().name());
        orderPersistenceEntity.setStatus(order.status().name());
        orderPersistenceEntity.setItems(mergeItems(orderPersistenceEntity, order));
        orderPersistenceEntity.setPlacedAt(order.placedAt());
        orderPersistenceEntity.setPaidAt(order.paidAt());
        orderPersistenceEntity.setReadyAt(order.readyAt());
        orderPersistenceEntity.setDeliveringAt(order.deliveringAt());
        orderPersistenceEntity.setDeliveryAt(order.deliveryAt());
        orderPersistenceEntity.setCancelAt(order.cancelAt());

        return orderPersistenceEntity;
    }

    private Set<OrderItemPersistenceEntity> mergeItems(OrderPersistenceEntity orderPersistenceEntity, Order order) {
        var orderItemsPersistenceEntity = orderPersistenceEntity.getItems();
        var orderItems = order.items();

        if (orderItemsPersistenceEntity.isEmpty()) {
            return orderItems.stream().map(this::fromDomainItem)
                    .collect(Collectors.toSet());
        }

        var orderItemsPersistenceEntityMap = orderItemsPersistenceEntity.stream()
                .collect(Collectors.toMap(OrderItemPersistenceEntity::getId, item -> item));

        return orderItems.stream().map(orderItem -> {
            var persistenceItem = orderItemsPersistenceEntityMap.getOrDefault(orderItem.id().value(), new OrderItemPersistenceEntity());
            return mergeItem(persistenceItem, orderItem);
        }).collect(Collectors.toSet());

    }

    private OrderItemPersistenceEntity fromDomainItem(OrderItem item) {
        return mergeItem(new OrderItemPersistenceEntity(), item);
    }

    private OrderItemPersistenceEntity mergeItem(OrderItemPersistenceEntity orderItemPersistenceEntity, OrderItem orderItem) {
        orderItemPersistenceEntity.setId(orderItem.orderId().value());
        orderItemPersistenceEntity.setProduct(ProductEmbeddable.builder()
                        .name(orderItem.product().name().value())
                        .description(orderItem.product().description().value())
                        .price(orderItem.product().price().value())
                        .build());
        orderItemPersistenceEntity.setQuantity(orderItem.quantity().value());
        orderItemPersistenceEntity.setTotalAmount(orderItem.totalAmount().value());

        return orderItemPersistenceEntity;
    }*/

}
