package br.com.ipet.ordering.infrastructure.persistence.order;

import br.com.ipet.ordering.application.order.query.OrderDetailOutput;
import br.com.ipet.ordering.application.order.query.OrderFilter;
import br.com.ipet.ordering.application.order.query.OrderQueryService;
import br.com.ipet.ordering.application.order.query.OrderSummaryOutput;
import br.com.ipet.ordering.application.util.Mapper;
import br.com.ipet.ordering.domain.model.order.OrderNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static br.com.ipet.ordering.infrastructure.persistence.order.OrderPersistenceSpecification.customerIdEquals;
import static br.com.ipet.ordering.infrastructure.persistence.order.OrderPersistenceSpecification.placedAtBetween;
import static br.com.ipet.ordering.infrastructure.persistence.order.OrderPersistenceSpecification.statusEquals;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderQueryServiceImpl implements OrderQueryService {

    private final OrderPersistenceRepository repository;
    private final Mapper mapper;

    @Override
    public OrderDetailOutput findById(UUID id) {
        var orderPersistenceEntity = repository.findById(id).orElseThrow(() -> new OrderNotFoundException(""));
        return mapper.convert(orderPersistenceEntity, OrderDetailOutput.class);
    }

    @Override
    public Page<OrderSummaryOutput> findAll(OrderFilter filter, Pageable pageable) {
        return repository.findAll(toSpecification(filter), pageable)
                .map(orderPersistenceEntity ->
                        mapper.convert(orderPersistenceEntity, OrderSummaryOutput.class));
    }

    private Specification<OrderPersistenceEntity> toSpecification(OrderFilter filter) {
        return customerIdEquals(filter.getCustomerId())
                .and(
                        placedAtBetween(filter.getStartTime(), filter.getEndTime())
                        .or(statusEquals(filter.getStatus()))
                );
    }
}
