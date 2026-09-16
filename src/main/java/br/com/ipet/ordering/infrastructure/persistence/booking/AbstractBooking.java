package br.com.ipet.ordering.infrastructure.persistence.booking;

import br.com.ipet.ordering.infrastructure.persistence.company.CompanyPersistenceEntity;
import br.com.ipet.ordering.infrastructure.persistence.customer.CustomerPersistenceEntity;
import br.com.ipet.ordering.infrastructure.persistence.order.BillingEmbeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(of = "id")
@MappedSuperclass
public class AbstractBooking {

    @Id
    @EqualsAndHashCode.Include
    private UUID id;

    @JoinColumn
    @ManyToOne(optional = false)
    private CompanyPersistenceEntity company;

    @JoinColumn
    @ManyToOne(optional = false)
    private CustomerPersistenceEntity customer;

    @Embedded
    private BillingEmbeddable billing;

    private String paymentMethod;
    private String paymentStatus;
    private Integer totalPets;
    private BigDecimal totalAmount;

    private OffsetDateTime requestedAt;
    private OffsetDateTime paidAt;
    private OffsetDateTime scheduledAt;
    private OffsetDateTime completedAt;
    private OffsetDateTime canceledAt;
    private OffsetDateTime refundedAt;
    private String cancelationReason;


}
