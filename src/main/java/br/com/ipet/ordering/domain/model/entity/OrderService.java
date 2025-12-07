package br.com.ipet.ordering.domain.model.entity;

import br.com.ipet.ordering.domain.model.valueobject.Money;
import br.com.ipet.ordering.domain.model.valueobject.OrderId;
import br.com.ipet.ordering.domain.model.valueobject.OrderServiceId;
import br.com.ipet.ordering.domain.model.valueobject.Quantity;
import br.com.ipet.ordering.domain.model.valueobject.Service;

public class OrderService {
    private OrderServiceId id;
    private OrderId orderId;
    private Service service;
    private Quantity quantity;
    private Money amount;
}
