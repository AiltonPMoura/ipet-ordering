package br.com.ipet.ordering.infrastructure.persistence.embedded;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class FullNameEmbedded {
    private String firstName;
    private String lastName;
}
