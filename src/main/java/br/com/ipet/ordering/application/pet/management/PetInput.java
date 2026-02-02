package br.com.ipet.ordering.application.pet.management;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PetInput {
    private UUID customerId;
    private String name;
    private String type;
    private String size;
    private String breed;
    private String gender;
    private Double weight;
    private Integer age;
}
