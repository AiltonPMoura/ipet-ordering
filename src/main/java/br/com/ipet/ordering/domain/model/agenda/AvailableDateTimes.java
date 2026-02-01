package br.com.ipet.ordering.domain.model.agenda;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public record AvailableDateTimes(LocalDate date,
                                 Map<Integer, List<Integer>> availableTimes) {
}
