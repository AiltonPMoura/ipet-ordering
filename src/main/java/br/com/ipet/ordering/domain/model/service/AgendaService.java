package br.com.ipet.ordering.domain.model.service;

import br.com.ipet.ordering.domain.model.entity.Agenda;
import br.com.ipet.ordering.domain.model.util.FieldValidator;

import java.time.LocalDate;
import java.util.List;

public class AgendaService {

    public List<LocalDate> calendar(Agenda agenda) {
        FieldValidator.requiresNonNull("agenda", agenda);

        if (agenda.isDraft() || agenda.isblocked()) {
            throw new RuntimeException("Agenda não disponível no momento");
        }

        return agenda.bookingByDates().stream()
                .filter(agenda::isWorkingDay)
                .filter(agenda::isNotStandBy)
                .toList();
    }

}
