package br.com.ipet.ordering.application.order.query;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface OrderQueryService {
    OrderDetailOutput findById(UUID id);
    Page<OrderSummaryOutput> findAll(OrderFilter filter, Pageable pageable);
}
