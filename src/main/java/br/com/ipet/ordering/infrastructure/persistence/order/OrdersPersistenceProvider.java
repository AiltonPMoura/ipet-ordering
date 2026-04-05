package br.com.ipet.ordering.infrastructure.persistence.order;

import br.com.ipet.ordering.domain.model.order.Order;
import br.com.ipet.ordering.domain.model.order.Orders;
import br.com.ipet.ordering.domain.model.order.OrderId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class OrdersPersistenceProvider implements Orders {
    private final OrderPersistenceRepository repository;
    private final OrderMapper orderMapper;
    private final OrderPersistenceMapper orderPersistenceMapper;

    @Override
    public Optional<Order> ofId(OrderId orderId) {
        var persistenceEntity = repository.findById(orderId.value());
        return persistenceEntity.map(orderMapper::toDomain);
    }

    @Override
    public boolean exists(OrderId orderId) {
        return repository.existsById(orderId.value());
    }

    @Override
    public void add(Order order) {
        repository.findById(order.id().value())
                .ifPresentOrElse(orderPersistenceEntity ->
                        update(orderPersistenceEntity, order),
                        () -> insert(order)
                );
    }

    private void insert(Order order) {
        var orderPersistenceEntity = orderPersistenceMapper.fromDomain(order);
        repository.saveAndFlush(orderPersistenceEntity);
    }

    private void update(OrderPersistenceEntity orderPersistenceEntity, Order order) {
        orderPersistenceEntity = orderPersistenceMapper.merge(orderPersistenceEntity, order);
        repository.saveAndFlush(orderPersistenceEntity);
    }

    @Override
    public long count() {
        return repository.count();
    }
}
