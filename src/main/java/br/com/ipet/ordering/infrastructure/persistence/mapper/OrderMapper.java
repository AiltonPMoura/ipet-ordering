package br.com.ipet.ordering.infrastructure.persistence.mapper;

import br.com.ipet.ordering.domain.model.order.Order;
import br.com.ipet.ordering.domain.model.order.OrderItem;
import br.com.ipet.ordering.domain.model.order.OrderStatus;
import br.com.ipet.ordering.domain.model.order.PaymentMethod;
import br.com.ipet.ordering.domain.model.customer.CompanyId;
import br.com.ipet.ordering.domain.model.customer.CustomerId;
import br.com.ipet.ordering.domain.model.commons.Money;
import br.com.ipet.ordering.domain.model.order.OrderId;
import br.com.ipet.ordering.domain.model.order.OrderItemId;
import br.com.ipet.ordering.domain.model.commons.Product;
import br.com.ipet.ordering.domain.model.commons.ProductDescription;
import br.com.ipet.ordering.domain.model.commons.ProductName;
import br.com.ipet.ordering.domain.model.commons.Quantity;
import br.com.ipet.ordering.infrastructure.persistence.embedded.ProductEmbeddable;
import br.com.ipet.ordering.infrastructure.persistence.entity.OrderItemPersistenceEntity;
import br.com.ipet.ordering.infrastructure.persistence.entity.OrderPersistenceEntity;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class OrderMapper {

    public Order toDomainEntity(OrderPersistenceEntity persistenceEntity) {
        return Order.existing()
                .id(new OrderId(persistenceEntity.getId()))
                .customerId(new CustomerId(persistenceEntity.getCustomerId()))
                .companyId(new CompanyId(persistenceEntity.getCompanyId()))
                .totalAmount(new Money(persistenceEntity.getTotalAmount()))
                .totalItems(new Quantity(persistenceEntity.getTotalItems()))
                .items(toItemsDomainEntity(persistenceEntity.getItems()))
                .paymentMethod(PaymentMethod.valueOf(persistenceEntity.getPaymentMethod()))
                .status(OrderStatus.valueOf(persistenceEntity.getStatus()))
                .placedAt(persistenceEntity.getPlacedAt())
                .readyAt(persistenceEntity.getReadyAt())
                .paidAt(persistenceEntity.getPaidAt())
                .deliveringAt(persistenceEntity.getDeliveringAt())
                .deliveryAt(persistenceEntity.getDeliveryAt())
                .cancelAt(persistenceEntity.getCancelAt())
                .build();
    }

    private Set<OrderItem> toItemsDomainEntity(Set<OrderItemPersistenceEntity> itemsPersistenceEntity) {
        return itemsPersistenceEntity.stream().map(itemPersistenceEntity -> OrderItem.existing()
                .id(new OrderItemId(itemPersistenceEntity.getId()))
                .orderId(new OrderId(itemPersistenceEntity.getOrderId()))
                .product(toProductValueObject(itemPersistenceEntity.getProduct()))
                .quantity(new Quantity(itemPersistenceEntity.getQuantity()))
                .totalAmount(new Money(itemPersistenceEntity.getTotalAmount()))
                .build()
        ).collect(Collectors.toSet());
    }

    private Product toProductValueObject(ProductEmbeddable productEmbeddable) {
        return Product.builder()
                .name(new ProductName(productEmbeddable.getName()))
                .description(new ProductDescription(productEmbeddable.getDescription()))
                .price(new Money(productEmbeddable.getPrice()))
                .build();
    }

}
