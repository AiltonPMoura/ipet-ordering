package br.com.ipet.ordering.domain.model.schedule;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record AvailableDay(LocalDate date, List<LocalTime> times) {
}
