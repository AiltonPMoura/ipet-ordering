package br.com.ipet.ordering.application.pet.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PetSummaryOutput {
    private UUID petId;
    private String name;
    private String type;
    private String breed;
    private String gender;
}
