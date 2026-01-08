package br.com.ipet.ordering.domain.model.service;

import br.com.ipet.ordering.domain.model.entity.Agenda;
import br.com.ipet.ordering.domain.model.entity.Customer;
import br.com.ipet.ordering.domain.model.repository.Agendas;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BookingService {

    private final Agendas agendas;

    public void reservar(Customer customer, Agenda agenda) {

    }

}
