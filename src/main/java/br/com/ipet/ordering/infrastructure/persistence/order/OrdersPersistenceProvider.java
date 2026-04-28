package br.com.ipet.ordering.infrastructure.persistence.order;

import br.com.ipet.ordering.domain.model.order.Order;
import br.com.ipet.ordering.domain.model.order.Orders;
import br.com.ipet.ordering.domain.model.order.OrderId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class OrdersPersistenceProvider implements Orders {

    private final OrderPersistenceRepository repository;
    private final OrderMapper orderMapper;
    private final OrderPersistenceMapper orderPersistenceMapper;

    @Override
    @Transactional(readOnly = true)
    public Optional<Order> ofId(OrderId orderId) {
        return repository.findById(orderId.value()).map(orderMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean exists(OrderId orderId) {
        return repository.existsById(orderId.value());
    }

    @Override
    @Transactional
    public void add(Order order) {
        repository.findById(order.id().value())
                .ifPresentOrElse(
                        orderPersistence -> this.update(orderPersistence, order),
                        () -> this.insert(order)
                );

        order.clearDomainEvents();
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return repository.count();
    }

    private void insert(Order order) {
        var orderPersistence = orderPersistenceMapper.fromDomain(order);
        repository.saveAndFlush(orderPersistence);
    }

    private void update(OrderPersistenceEntity orderPersistence, Order order) {
        orderPersistence = orderPersistenceMapper.merge(orderPersistence, order);
        repository.saveAndFlush(orderPersistence);
    }
}
