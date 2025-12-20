package br.com.ipet.ordering.infrastructure.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(of = "id")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "product")
public class ProductPersistenceEntity {

    @Id
    private UUID id;

    @JoinColumn
    @ManyToOne(optional = false)
    private CompanyPersistenceEntity company;

    private String name;
    private String description;
    private BigDecimal price;

    public UUID getCompanyId() {
        return this.company.getId();
    }
}
