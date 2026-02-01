package br.com.ipet.ordering.infrastructure.persistence.provider;

import br.com.ipet.ordering.domain.model.order.Order;
import br.com.ipet.ordering.domain.model.order.Orders;
import br.com.ipet.ordering.domain.model.order.OrderId;
import br.com.ipet.ordering.infrastructure.persistence.mapper.OrderMapper;
import br.com.ipet.ordering.infrastructure.persistence.mapper.OrderPersistenceEntityMapper;
import br.com.ipet.ordering.infrastructure.persistence.repository.OrderPersistenceEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class OrdersPersistenceProvider implements Orders {

    private final OrderPersistenceEntityRepository repository;
    private final OrderMapper orderMapper;
    private final OrderPersistenceEntityMapper orderPersistenceEntityMapper;

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
        var orderPersistenceEntity = orderPersistenceEntityMapper.fromDomain(aggregateRoot);
        repository.saveAndFlush(orderPersistenceEntity);
    }

    @Override
    public int count() {
        return 0;
    }
}
