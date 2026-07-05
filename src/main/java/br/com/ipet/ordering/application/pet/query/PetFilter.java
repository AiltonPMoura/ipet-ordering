package br.com.ipet.ordering.application.pet.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PetFilter {
    private String name;
    private String type;
    private String size;
    private String breed;
}
