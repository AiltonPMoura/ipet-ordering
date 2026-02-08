package br.com.ipet.ordering.application.customer.query;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerSummaryOutput {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private Integer celPhone;
    private OffsetDateTime registerAt;
}
