package br.com.ipet.ordering.application.customer.management;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerUpdateInput {
    private String firstName;
    private String lastName;
    private String phone;
    private String document;
    private LocalDate birthDate;
}
