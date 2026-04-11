package br.com.ipet.ordering.infrastructure.persistence.order;

import br.com.ipet.ordering.domain.model.commons.valueobject.Address;
import br.com.ipet.ordering.domain.model.commons.valueobject.Product;
import br.com.ipet.ordering.domain.model.order.Billing;
import br.com.ipet.ordering.domain.model.order.DeliveryCompany;
import br.com.ipet.ordering.domain.model.order.Order;
import br.com.ipet.ordering.domain.model.order.OrderItem;
import br.com.ipet.ordering.domain.model.order.Shipping;
import br.com.ipet.ordering.infrastructure.persistence.commons.AddressEmbeddable;
import br.com.ipet.ordering.infrastructure.persistence.commons.ProductEmbeddable;
import br.com.ipet.ordering.infrastructure.persistence.company.CompanyPersistenceRepository;
import br.com.ipet.ordering.infrastructure.persistence.customer.CustomerPersistenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderPersistenceMapper {

    private final CustomerPersistenceRepository customerPersistenceRepository;
    private final CompanyPersistenceRepository companyPersistenceRepository;

    public OrderPersistenceEntity fromDomain(Order order) {
        return merge(new OrderPersistenceEntity(), order);
    }

    public OrderPersistenceEntity merge(OrderPersistenceEntity orderPersistence, Order order) {
        orderPersistence.setId(order.id().value());
        orderPersistence.setCustomer(customerPersistenceRepository.getReferenceById(order.customerId().value()));
        orderPersistence.setCompany(companyPersistenceRepository.getReferenceById(order.companyId().value()));
        orderPersistence.setTotalAmount(order.totalAmount().value());
        orderPersistence.setTotalItems(order.totalItems().value());
        orderPersistence.setPaymentMethod(order.paymentMethod().name());
        orderPersistence.setStatus(order.status().name());
        orderPersistence.setItems(this.mergeItems(orderPersistence, order));
        orderPersistence.setBilling(this.toBillingEmbeddable(order.billing()));
        orderPersistence.setShipping(this.toShippingEmbeddable(order.shipping()));
        orderPersistence.setDeliveryCompany(this.toDeliveryCompany(order.deliveryCompany()));
        orderPersistence.setPlacedAt(order.placedAt());
        orderPersistence.setPaidAt(order.paidAt());
        orderPersistence.setReadyAt(order.readyAt());
        orderPersistence.setOutForDeliveryAt(order.outForDeliveryAt());
        orderPersistence.setDeliveredAt(order.deliveredAt());
        orderPersistence.setCanceledAt(order.canceledAt());
        return orderPersistence;
    }

    private Set<OrderItemPersistenceEntity> mergeItems(OrderPersistenceEntity orderPersistence, Order order) {
        var orderItemsPersistence = orderPersistence.getItems();
        var orderItems = order.items();

        if (orderItemsPersistence.isEmpty())
            return orderItems.stream().map(this::fromDomainItem).collect(Collectors.toSet());

        var orderItemsPersistenceMap = orderItemsPersistence.stream()
                .collect(Collectors.toMap(OrderItemPersistenceEntity::getId, item -> item));

        return orderItems.stream().map(orderItem -> {
            var orderItemPersistence = orderItemsPersistenceMap.getOrDefault(orderItem.id().value(), new OrderItemPersistenceEntity());
            return mergeItem(orderItemPersistence, orderItem);
        }).collect(Collectors.toSet());

    }

    private OrderItemPersistenceEntity fromDomainItem(OrderItem orderItem) {
        return mergeItem(new OrderItemPersistenceEntity(), orderItem);
    }

    private OrderItemPersistenceEntity mergeItem(OrderItemPersistenceEntity orderItemPersistence, OrderItem orderItem) {
        orderItemPersistence.setId(orderItem.id().value());
        orderItemPersistence.setProduct(this.toProductEmbeddable(orderItem.product()));
        orderItemPersistence.setQuantity(orderItem.quantity().value());
        orderItemPersistence.setTotalAmount(orderItem.totalAmount().value());
        return orderItemPersistence;
    }

    private ProductEmbeddable toProductEmbeddable(Product product) {
        return ProductEmbeddable.builder()
                .productId(product.id().value())
                .name(product.name().value())
                .description(product.description().value())
                .price(product.price().value())
                .build();
    }

    private BillingEmbeddable toBillingEmbeddable(Billing billing) {
        return BillingEmbeddable.builder()
                .firstName(billing.fullName().firstName())
                .lastName(billing.fullName().lastName())
                .document(billing.document().value())
                .phone(billing.phone().value())
                .email(billing.email().value())
                .address(this.toAddressEmbeddable(billing.address()))
                .build();
    }

    private ShippingEmbeddable toShippingEmbeddable(Shipping shipping) {
        var recipient = shipping.recipient();

        return ShippingEmbeddable.builder()
                .cost(shipping.cost().value())
                .expectedDate(shipping.expectedDate())
                .recipient(RecipientEmbeddable.builder()
                        .firstName(recipient.fullName().firstName())
                        .lastName(recipient.fullName().lastName())
                        .document(recipient.document().value())
                        .phone(recipient.phone().value())
                        .build()
                )
                .address(this.toAddressEmbeddable(shipping.address()))
                .build();
    }

    private DeliveryCompanyEmbeddable toDeliveryCompany(DeliveryCompany deliveryCompany) {
        return DeliveryCompanyEmbeddable.builder()
                .companyName(deliveryCompany.companyName().value())
                .document(deliveryCompany.document().value())
                .phone(deliveryCompany.phone().value())
                .email(deliveryCompany.email().value())
                .address(this.toAddressEmbeddable(deliveryCompany.address()))
                .build();
    }

    private AddressEmbeddable toAddressEmbeddable(Address address) {
        return AddressEmbeddable.builder()
                .street(address.street())
                .number(address.number())
                .neighborhood(address.neighborhood())
                .city(address.city())
                .state(address.state())
                .complement(address.complement())
                .zipCode(address.zipCode().value())
                .build();
    }

}
