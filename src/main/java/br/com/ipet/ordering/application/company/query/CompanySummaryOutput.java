package br.com.ipet.ordering.application.company.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompanySummaryOutput {
    private UUID id;
    private String companyName;
    private Integer celPhone;
    private String email;
}
