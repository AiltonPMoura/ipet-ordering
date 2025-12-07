package br.com.ipet.ordering.infrastructure.persistence.mapper;

import br.com.ipet.ordering.domain.model.entity.Order;
import br.com.ipet.ordering.domain.model.entity.OrderStatus;
import br.com.ipet.ordering.domain.model.entity.PaymentMethod;
import br.com.ipet.ordering.domain.model.valueobject.CompanyId;
import br.com.ipet.ordering.domain.model.valueobject.CustumerId;
import br.com.ipet.ordering.domain.model.valueobject.Money;
import br.com.ipet.ordering.domain.model.valueobject.OrderId;
import br.com.ipet.ordering.domain.model.valueobject.Quantity;
import br.com.ipet.ordering.infrastructure.persistence.entity.OrderPersistenceEntity;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    public Order toDomainEntity(OrderPersistenceEntity persistenceEntity) {
        return Order.existing()
                .id(new OrderId(persistenceEntity.getId()))
                .custumerId(new CustumerId(persistenceEntity.getCustomerId()))
                .companyId(new CompanyId(persistenceEntity.getCompanyId()))
                .totalAmount(new Money(persistenceEntity.getTotalAmount()))
                .totalItems(new Quantity(persistenceEntity.getTotalItems()))
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

}
