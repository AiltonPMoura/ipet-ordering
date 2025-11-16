package br.com.ipet.ordering.domain.model;

import br.com.ipet.ordering.domain.valueobject.*;
import br.com.ipet.ordering.domain.valueobject.Service;

public class OrderService {
    private OrderServiceId id;
    private OrderId orderId;
    private Service service;
    private Quantity quantity;
    private Money amount;
}
