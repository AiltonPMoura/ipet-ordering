package br.com.ipet.ordering.application.pet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PetUpdateInput {
    private String name;
    private String type;
    private String size;
    private String breed;
    private String gender;
    private Double weight;
    private Integer age;
}
