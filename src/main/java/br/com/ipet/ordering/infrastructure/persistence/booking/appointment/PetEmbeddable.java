package br.com.ipet.ordering.infrastructure.persistence.booking.appointment;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class PetEmbeddable {

    private UUID id;
    private String name;
    private String type;
    private String breed;
    private String gender;
    private String size;
    private Double weight;
    private Integer age;

}
