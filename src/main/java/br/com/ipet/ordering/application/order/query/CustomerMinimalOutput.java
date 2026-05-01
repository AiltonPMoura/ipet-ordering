package br.com.ipet.ordering.application.order.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerMinimalOutput {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String document;
}
