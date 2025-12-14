package br.com.ipet.ordering.domain.model.repository;

import br.com.ipet.ordering.domain.model.entity.Order;
import br.com.ipet.ordering.domain.model.valueobject.OrderId;

public interface Orders extends Repository<Order, OrderId> {
}
