package br.com.ipet.ordering.infrastructure.persistence.provider;

import br.com.ipet.ordering.domain.model.entity.Order;
import br.com.ipet.ordering.domain.model.repository.Orders;
import br.com.ipet.ordering.domain.model.valueobject.OrderId;
import br.com.ipet.ordering.infrastructure.persistence.mapper.OrderMapper;
import br.com.ipet.ordering.infrastructure.persistence.repository.OrderPersistenceEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class OrdersPersistenceProvider implements Orders {

    private final OrderPersistenceEntityRepository repository;
    private final OrderMapper orderMapper;

    @Override
    public Optional<Order> ofId(OrderId orderId) {
        var persistenceEntity = repository.findById(orderId.value());
        return persistenceEntity.map(orderMapper::toDomainEntity);
    }

    @Override
    public boolean exists(OrderId orderId) {
        return false;
    }

    @Override
    public void add(Order aggregateRoot) {

    }

    @Override
    public int count() {
        return 0;
    }
}
