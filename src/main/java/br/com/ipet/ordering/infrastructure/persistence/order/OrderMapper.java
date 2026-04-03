package br.com.ipet.ordering.infrastructure.persistence.order;

//import br.com.ipet.ordering.infrastructure.persistence.embedded.ProductEmbeddable;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    /*public Order toDomainEntity(OrderPersistenceEntity persistenceEntity) {
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
    }*/

}
