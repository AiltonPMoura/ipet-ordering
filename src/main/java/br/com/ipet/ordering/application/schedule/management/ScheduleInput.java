package br.com.ipet.ordering.application.schedule.management;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleInput {
    private UUID companyId;
    private String name;
    private String service;
}
