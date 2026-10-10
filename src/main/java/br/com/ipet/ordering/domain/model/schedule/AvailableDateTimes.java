package br.com.ipet.ordering.domain.model.schedule;

import java.time.LocalDate;
import java.util.List;

public record AvailableDateTimes(LocalDate startDate,
                                 LocalDate endDate,
                                 List<AvailableDay> availableDays) {
}
