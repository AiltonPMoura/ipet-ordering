package br.com.ipet.ordering.infrastructure.persistence.order;

import br.com.ipet.ordering.domain.model.commons.document.DocumentFactory;
import br.com.ipet.ordering.domain.model.commons.valueobject.Address;
import br.com.ipet.ordering.domain.model.commons.valueobject.Company;
import br.com.ipet.ordering.domain.model.commons.valueobject.Customer;
import br.com.ipet.ordering.domain.model.commons.valueobject.Email;
import br.com.ipet.ordering.domain.model.commons.valueobject.FullName;
import br.com.ipet.ordering.domain.model.commons.valueobject.Money;
import br.com.ipet.ordering.domain.model.commons.valueobject.Phone;
import br.com.ipet.ordering.domain.model.commons.valueobject.Quantity;
import br.com.ipet.ordering.domain.model.commons.valueobject.ZipCode;
import br.com.ipet.ordering.domain.model.company.CompanyId;
import br.com.ipet.ordering.domain.model.company.CompanyName;
import br.com.ipet.ordering.domain.model.customer.CustomerId;
import br.com.ipet.ordering.domain.model.commons.valueobject.Billing;
import br.com.ipet.ordering.domain.model.order.DeliveryCompany;
import br.com.ipet.ordering.domain.model.order.Order;
import br.com.ipet.ordering.domain.model.order.OrderId;
import br.com.ipet.ordering.domain.model.order.OrderItem;
import br.com.ipet.ordering.domain.model.order.OrderItemId;
import br.com.ipet.ordering.domain.model.order.OrderStatus;
import br.com.ipet.ordering.domain.model.order.OrderPaymentMethod;
import br.com.ipet.ordering.domain.model.order.Shipping;
import br.com.ipet.ordering.domain.model.product.Product;
import br.com.ipet.ordering.domain.model.product.ProductDescription;
import br.com.ipet.ordering.domain.model.product.ProductId;
import br.com.ipet.ordering.domain.model.product.ProductName;
import br.com.ipet.ordering.infrastructure.persistence.commons.AddressEmbeddable;
import br.com.ipet.ordering.infrastructure.persistence.commons.ProductEmbeddable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderMapper {

    public Order toDomain(OrderPersistenceEntity orderPersistence) {
        return Order.existing()
                .id(new OrderId(orderPersistence.getId()))
                .customerId(new CustomerId(orderPersistence.getCustomerId()))
                .companyId(new CompanyId(orderPersistence.getCompanyId()))
                .items(this.toItems(orderPersistence.getItems()))
                .totalAmount(new Money(orderPersistence.getTotalAmount()))
                .totalItems(new Quantity(orderPersistence.getTotalItems()))
                .orderPaymentMethod(OrderPaymentMethod.valueOf(orderPersistence.getPaymentMethod()))
                .status(OrderStatus.valueOf(orderPersistence.getStatus()))
                .billing(this.toBilling(orderPersistence.getBilling()))
                .shipping(this.toShipping(orderPersistence.getShipping()))
                .deliveryCompany(this.toDeliveryCompany(orderPersistence.getDeliveryCompany()))
                .placedAt(orderPersistence.getPlacedAt())
                .paidAt(orderPersistence.getPaidAt())
                .readyAt(orderPersistence.getReadyAt())
                .outForDeliveryAt(orderPersistence.getOutForDeliveryAt())
                .deliveredAt(orderPersistence.getDeliveredAt())
                .canceledAt(orderPersistence.getCanceledAt())
                .build();
    }

    private Set<OrderItem> toItems(Set<OrderItemPersistenceEntity> itemsPersistence) {
        return itemsPersistence.stream().map(itemPersistence ->
                OrderItem.existing()
                        .id(new OrderItemId(itemPersistence.getId()))
                        .orderId(new OrderId(itemPersistence.getOrderId()))
                        .product(this.toProduct(itemPersistence.getProduct()))
                        .quantity(new Quantity(itemPersistence.getQuantity()))
                        .totalAmount(new Money(itemPersistence.getTotalAmount()))
                        .build()
        ).collect(Collectors.toSet());
    }

    private Product toProduct(ProductEmbeddable productEmbeddable) {
        return Product.builder()
                .id(new ProductId(productEmbeddable.getProductId()))
                .name(new ProductName(productEmbeddable.getName()))
                .description(new ProductDescription(productEmbeddable.getDescription()))
                .price(new Money(productEmbeddable.getPrice()))
                .build();
    }

    private Shipping toShipping(ShippingEmbeddable shippingEmbeddable) {
        return Shipping.builder()
                .cost(new Money(shippingEmbeddable.getCost()))
                .expectedDate(shippingEmbeddable.getExpectedDate())
                .address(this.toAddress(shippingEmbeddable.getAddress()))
                .build();
    }

    private Billing toBilling(BillingEmbeddable billingEmbeddable) {
        return new Billing(Customer.builder()
                .fullName(new FullName(billingEmbeddable.getFirstName(), billingEmbeddable.getLastName()))
                .document(DocumentFactory.from(billingEmbeddable.getDocument()))
                .phone(new Phone(billingEmbeddable.getPhone()))
                .email(new Email(billingEmbeddable.getEmail()))
                .address(this.toAddress(billingEmbeddable.getAddress()))
                .build()
        );
    }

    private DeliveryCompany toDeliveryCompany(DeliveryCompanyEmbeddable deliveryCompany) {
        return new DeliveryCompany(Company.builder()
                .companyName(new CompanyName(deliveryCompany.getCompanyName()))
                .document(DocumentFactory.from(deliveryCompany.getDocument()))
                .phone(new Phone(deliveryCompany.getPhone()))
                .email(new Email(deliveryCompany.getEmail()))
                .address(this.toAddress(deliveryCompany.getAddress()))
                .build()
        );
    }

    private Address toAddress(AddressEmbeddable addressEmbeddable) {
        return Address.builder()
                .street(addressEmbeddable.getStreet())
                .number(addressEmbeddable.getNumber())
                .neighborhood(addressEmbeddable.getNeighborhood())
                .city(addressEmbeddable.getCity())
                .complement(addressEmbeddable.getComplement())
                .zipCode(new ZipCode(addressEmbeddable.getZipCode()))
                .build();
    }

}
